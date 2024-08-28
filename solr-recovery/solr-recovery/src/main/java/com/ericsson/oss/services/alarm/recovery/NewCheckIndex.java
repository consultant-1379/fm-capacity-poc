/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ericsson.oss.services.alarm.recovery;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.lucene.codecs.Codec;
import org.apache.lucene.codecs.lucene3x.Lucene3xSegmentInfoFormat;
import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.CheckIndex;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.FieldInfos;
import org.apache.lucene.index.SegmentCommitInfo;
import org.apache.lucene.index.SegmentInfos;
import org.apache.lucene.index.SegmentReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.StringHelper;

/**
 * Basic tool and API to check the health of an index and
 * write a new segments file that removes reference to
 * problematic segments.
 *
 * <p>As this tool checks every byte in the index, on a large
 * index it can take quite a long time to run.
 *
 * @lucene.experimental Please make a complete backup of your
 * index before using this to exorcise corrupted documents from your index!
 */
public class NewCheckIndex {
    public static List<String> segmentsChecked = new ArrayList<>();
    static boolean missingSegments = false;
    static boolean cantOpenSegments = false;
    static boolean missingSegmentVersion = false;
    static boolean partial = false;
    static boolean toolOutOfDate;
    static List<String> onlySegments = null;


    public static void main(String[] args) throws IOException, InterruptedException {
        NumberFormat nf = NumberFormat.getInstance(Locale.ROOT);
        Status result = new Status();
        SegmentInfos sis = new SegmentInfos();
        Directory dir = null;

        dir = dir = FSDirectory.open(new File("/ericsson/enm/solr/data/index"));
        String[] files = dir.listAll();
        try {
            // Do not use SegmentInfos.read(Directory) since the spooky
            // retrying it does is not necessary here (we hold the write lock):
            System.out.println("sis.read " + dir);
            sis.read(dir);
        } catch (Throwable t) {
            System.out.println("ERROR: could not read any segments file in directory " + t);
            missingSegments = true;
            return;
        }

        // find the oldest and newest segment versions
        String oldest = Integer.toString(Integer.MAX_VALUE), newest = Integer.toString(Integer.MIN_VALUE);
        String oldSegs = null;
        boolean foundNonNullVersion = false;
        Comparator<String> versionComparator = StringHelper.getVersionComparator();
        for (SegmentCommitInfo si : sis) {
            String version = si.info.getVersion();
            if (version == null) {
                // pre-3.1 segment
                oldSegs = "pre-3.1";
            } else {
                foundNonNullVersion = true;
                if (versionComparator.compare(version, oldest) < 0) {
                    oldest = version;
                }
                if (versionComparator.compare(version, newest) > 0) {
                    newest = version;
                }
            }
        }
        final int numSegments = sis.size();
        final String segmentsFileName = sis.getSegmentsFileName();
        // note: we only read the format byte (required preamble) here!
        IndexInput input = null;
        try {
            input = dir.openInput(segmentsFileName, IOContext.READONCE);
        } catch (Throwable t) {
            System.out.println("ERROR: could not open segments file in directory" + t);
            cantOpenSegments = true;
            return;
        }
        int format = 0;
        try {
            format = input.readInt();
        } catch (Throwable t) {
            System.out.println("ERROR: could not read segment file version in directory" + t);
            missingSegmentVersion = true;
            return;
        } finally {
            if (input != null) {
                input.close();
            }
        }

        String sFormat = "";
        boolean skip = false;

        final Map<String, String> userData = sis.getUserData();
        String userDataString;
        if (sis.getUserData().size() > 0) {
            userDataString = " userData=" + sis.getUserData();
        } else {
            userDataString = "";
        }

        String versionString = null;
        if (oldSegs != null) {
            if (foundNonNullVersion) {
                versionString = "versions=[" + oldSegs + " .. " + newest + "]";
            } else {
                versionString = "version=" + oldSegs;
            }
        } else {
            versionString = oldest.equals(newest) ? ("version=" + oldest) : ("versions=[" + oldest + " .. " + newest + "]");
        }

        System.out.println("Segments file=" + segmentsFileName + " numSegments=" + numSegments
                                   + " " + versionString + " format=" + sFormat + userDataString);

        if (onlySegments != null) {
            partial = true;
            System.out.print("\nChecking only these segments:");
            for (String s : onlySegments) {
                System.out.print(" " + s);
            }
            segmentsChecked.addAll(onlySegments);
            System.out.println(":");
        }

        if (skip) {
            System.out.println("\nERROR: this index appears to be created by a newer version of Lucene than this tool was compiled on; please "
                                       + "re-compile this tool"
                                       + " on the matching version of Lucene; exiting");
            toolOutOfDate = true;
            return;
        }

        final SegmentInfos newSegments = sis.clone();
        newSegments.clear();
        int maxSegmentName = -1;

        for (int i = 0; i < numSegments; i++) {
            final SegmentCommitInfo info = sis.info(i);
            int segmentName = Integer.parseInt(info.info.name.substring(1), Character.MAX_RADIX);
            if (segmentName > maxSegmentName) {
                maxSegmentName = segmentName;
            }
            if (onlySegments != null && !onlySegments.contains(info.info.name)) {
                continue;
            }
            //            CheckIndex.Status.SegmentInfoStatus segInfoStat = new CheckIndex.Status.SegmentInfoStatus();
            //            final MessageHeader segmentInfos = new MessageHeader();
            //            segmentInfos.add(segInfoStat);
            System.out.println("  " + (1 + i) + " of " + numSegments + ": name=" + info.info.name + " docCount=" + info.info.getDocCount());
            SegmentInfoStatus segInfoStat = new SegmentInfoStatus();
            segInfoStat.name = info.info.name;
            segInfoStat.docCount = info.info.getDocCount();

            final String version = info.info.getVersion();
            if (info.info.getDocCount() <= 0 && version != null && versionComparator.compare(version, "4.5") >= 0) {
                throw new RuntimeException("illegal number of documents: maxDoc=" + info.info.getDocCount());
            }

            int toLoseDocCount = info.info.getDocCount();

            AtomicReader reader = null;

            try {
                final Codec codec = info.info.getCodec();
                System.out.println("    codec=" + codec);
                segInfoStat.codec = codec;
                System.out.println("    compound=" + info.info.getUseCompoundFile());
                segInfoStat.compound = info.info.getUseCompoundFile();
                System.out.println("    numFiles=" + info.files().size());
                segInfoStat.numFiles = info.files().size();
                segInfoStat.sizeMB = info.sizeInBytes() / (1024. * 1024.);
                if (info.info.getAttribute(Lucene3xSegmentInfoFormat.DS_OFFSET_KEY) == null) {
                    // don't print size in bytes if its a 3.0 segment with shared docstores
                    System.out.println("    size (MB)=" + nf.format(segInfoStat.sizeMB));
                }
                Map<String, String> diagnostics = info.info.getDiagnostics();
                segInfoStat.diagnostics = diagnostics;
                if (diagnostics.size() > 0) {
                    System.out.println("    diagnostics = " + diagnostics);
                }

                if (!info.hasDeletions()) {
                    System.out.println("    no deletions");
                    segInfoStat.hasDeletions = false;
                } else {
                    System.out.println("    has deletions [delGen=" + info.getDelGen() + "]");
                    segInfoStat.hasDeletions = true;
                    segInfoStat.deletionsGen = info.getDelGen();
                }

                System.out.println("    test: open reader.........");

                reader = new SegmentReader(info, DirectoryReader.DEFAULT_TERMS_INDEX_DIVISOR, IOContext.DEFAULT);
                System.out.println("OK");

                segInfoStat.openReaderPassed = true;

                System.out.println("    test: check integrity.....");

                reader.checkIntegrity();
                System.out.println("OK");

                System.out.println("    test: check live docs.....");

                final int numDocs = reader.numDocs();
                toLoseDocCount = numDocs;
                if (reader.hasDeletions()) {
                    if (reader.numDocs() != info.info.getDocCount() - info.getDelCount()) {
                        throw new RuntimeException(
                                "delete count mismatch: info=" + (info.info.getDocCount() - info.getDelCount()) + " vs reader=" + reader.numDocs());
                    }
                    if ((info.info.getDocCount() - reader.numDocs()) > reader.maxDoc()) {
                        throw new RuntimeException("too many deleted docs: maxDoc()=" + reader.maxDoc() + " vs del count=" + (info.info.getDocCount()
                                - reader.numDocs()));
                    }
                    if (info.info.getDocCount() - numDocs != info.getDelCount()) {
                        throw new RuntimeException(
                                "delete count mismatch: info=" + info.getDelCount() + " vs reader=" + (info.info.getDocCount() - numDocs));
                    }
                    Bits liveDocs = reader.getLiveDocs();
                    if (liveDocs == null) {
                        throw new RuntimeException("segment should have deletions, but liveDocs is null");
                    } else {
                        int numLive = 0;
                        for (int j = 0; j < liveDocs.length(); j++) {
                            if (liveDocs.get(j)) {
                                numLive++;
                            }
                        }
                        if (numLive != numDocs) {
                            throw new RuntimeException("liveDocs count mismatch: info=" + numDocs + ", vs bits=" + numLive);
                        }
                    }

                    segInfoStat.numDeleted = info.info.getDocCount() - numDocs;
                    System.out.println("OK [" + (segInfoStat.numDeleted) + " deleted docs]");
                } else {
                    if (info.getDelCount() != 0) {
                        throw new RuntimeException(
                                "delete count mismatch: info=" + info.getDelCount() + " vs reader=" + (info.info.getDocCount() - numDocs));
                    }
                    Bits liveDocs = reader.getLiveDocs();
                    if (liveDocs != null) {
                        // its ok for it to be non-null here, as long as none are set right?
                        for (int j = 0; j < liveDocs.length(); j++) {
                            if (!liveDocs.get(j)) {
                                throw new RuntimeException("liveDocs mismatch: info says no deletions but doc " + j + " is deleted.");
                            }
                        }
                    }
                    System.out.println("OK");
                }
                if (reader.maxDoc() != info.info.getDocCount()) {
                    throw new RuntimeException("SegmentReader.maxDoc() " + reader.maxDoc() + " != SegmentInfos.docCount " + info.info.getDocCount());
                }

                // Test getFieldInfos()
                System.out.println("    test: fields..............");

                FieldInfos fieldInfos = reader.getFieldInfos();
                System.out.println("OK [" + fieldInfos.size() + " fields]");
                segInfoStat.numFields = fieldInfos.size();

/*                // Test Field Norms
                segInfoStat.fieldNormStatus = testFieldNorms(reader, infoStream);

                // Test the Term Index
                segInfoStat.termIndexStatus = testPostings(reader, infoStream, verbose);

                // Test Stored Fields
                segInfoStat.storedFieldStatus = testStoredFields(reader, infoStream);

                // Test Term Vectors
                segInfoStat.termVectorStatus = testTermVectors(reader, infoStream, verbose, crossCheckTermVectors);

                segInfoStat.docValuesStatus = testDocValues(reader, infoStream);*/

                // Rethrow the first exception we encountered
                //  This will cause stats for failed segments to be incremented properly
                if (segInfoStat.fieldNormStatus.error != null) {
                    throw new RuntimeException("Field Norm test failed");
                } else if (segInfoStat.termIndexStatus.error != null) {
                    throw new RuntimeException("Term Index test failed");
                } else if (segInfoStat.storedFieldStatus.error != null) {
                    throw new RuntimeException("Stored Field test failed");
                } else if (segInfoStat.termVectorStatus.error != null) {
                    throw new RuntimeException("Term Vector test failed");
                } else if (segInfoStat.docValuesStatus.error != null) {
                    throw new RuntimeException("DocValues test failed");
                }

                System.out.println();
            } catch (Throwable t) {
                System.out.println("FAILED");
                String comment;
                comment = "fixIndex() would remove reference to this segment";
                System.out.println("    WARNING: " + comment + "; full exception:" + t);

                System.out.println();
                result.totLoseDocCount += toLoseDocCount;
                result.numBadSegments++;
                continue;
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }

            // Keeper
            result.newSegments.add(info.clone());
        }

        if (0 == result.numBadSegments) {
            result.clean = true;
        } else {
            System.out.println(
                    "WARNING: " + result.numBadSegments + " broken segments (containing " + result.totLoseDocCount + " documents) detected");
        }

        if (!(result.validCounter = (result.maxSegmentName < sis.counter))) {
            result.clean = false;
            result.newSegments.counter = result.maxSegmentName + 1;
            System.out.println("ERROR: Next segment name counter " + sis.counter + " is not greater than max segment name " + result.maxSegmentName);
        }

        if (result.clean) {
            System.out.println("No problems were detected with this index.\n");
        }
    }


    public static class SegmentInfoStatus {

        /**
         * Name of the segment.
         */
        public String name;
        /**
         * Codec used to read this segment.
         */
        public Codec codec;
        /**
         * Document count (does not take deletions into account).
         */
        public int docCount;
        /**
         * True if segment is compound file format.
         */
        public boolean compound;
        /**
         * Number of files referenced by this segment.
         */
        public int numFiles;
        /**
         * Net size (MB) of the files referenced by this
         * segment.
         */
        public double sizeMB;
        /**
         * Doc store offset, if this segment shares the doc
         * store files (stored fields and term vectors) with
         * other segments.  This is -1 if it does not share.
         */
        public int docStoreOffset = -1;
        /**
         * String of the shared doc store segment, or null if
         * this segment does not share the doc store files.
         */
        public String docStoreSegment;
        /**
         * True if the shared doc store files are compound file
         * format.
         */
        public boolean docStoreCompoundFile;
        /**
         * True if this segment has pending deletions.
         */
        public boolean hasDeletions;
        /**
         * Current deletions generation.
         */
        public long deletionsGen;
        /**
         * Number of deleted documents.
         */
        public int numDeleted;
        /**
         * True if we were able to open an AtomicReader on this
         * segment.
         */
        public boolean openReaderPassed;
        /**
         * Map that includes certain
         * debugging details that IndexWriter records into
         * each segment it creates
         */
        public Map<String, String> diagnostics;
        /**
         * Status for testing of field norms (null if field norms could not be tested).
         */
        public CheckIndex.Status.FieldNormStatus fieldNormStatus;
        /**
         * Status for testing of indexed terms (null if indexed terms could not be tested).
         */
        public CheckIndex.Status.TermIndexStatus termIndexStatus;
        /**
         * Status for testing of stored fields (null if stored fields could not be tested).
         */
        public CheckIndex.Status.StoredFieldStatus storedFieldStatus;
        /**
         * Status for testing of term vectors (null if term vectors could not be tested).
         */
        public CheckIndex.Status.TermVectorStatus termVectorStatus;
        /**
         * Status for testing of DocValues (null if DocValues could not be tested).
         */
        public CheckIndex.Status.DocValuesStatus docValuesStatus;
        /**
         * Number of fields in this segment.
         */
        int numFields;

        SegmentInfoStatus() {
        }
    }

    public static class Status {

        Status() {
        }

        /** True if no problems were found with the index. */
        public boolean clean;

        /** True if we were unable to locate and load the segments_N file. */
        public boolean missingSegments;

        /** True if we were unable to open the segments_N file. */
        public boolean cantOpenSegments;

        /** True if we were unable to read the version number from segments_N file. */
        public boolean missingSegmentVersion;

        /** Name of latest segments_N file in the index. */
        public String segmentsFileName;

        /** Number of segments in the index. */
        public int numSegments;

        /** Empty unless you passed specific segments list to check as optional 3rd argument.
         *  @see CheckIndex#checkIndex(List) */
        public List<String> segmentsChecked = new ArrayList<>();

        /** True if the index was created with a newer version of Lucene than the CheckIndex tool. */
        public boolean toolOutOfDate;

        /** List of {@link CheckIndex.Status.SegmentInfoStatus} instances, detailing status of each segment. */
        public List<CheckIndex.Status.SegmentInfoStatus> segmentInfos = new ArrayList<>();

        /** Directory index is in. */
        public Directory dir;

        /**
         * SegmentInfos instance containing only segments that
         * had no problems (this is used with the {@link CheckIndex#fixIndex}
         * method to repair the index.
         */
        SegmentInfos newSegments;

        /** How many documents will be lost to bad segments. */
        public int totLoseDocCount;

        /** How many bad segments were found. */
        public int numBadSegments;

        /** True if we checked only specific segments ({@link
         *
         * argument). */
        public boolean partial;

        /** The greatest segment name. */
        public int maxSegmentName;

        /** Whether the SegmentInfos.counter is greater than any of the segments' names. */
        public boolean validCounter;

        /** Holds the userData of the last commit in the index */
        public Map<String, String> userData;

        /** Holds the status of each segment in the index.
         *  See {@link #segmentInfos}.
         *
         * @lucene.experimental
         */
        public static class SegmentInfoStatus {

            SegmentInfoStatus() {
            }

            /** Name of the segment. */
            public String name;

            /** Codec used to read this segment. */
            public Codec codec;

            /** Document count (does not take deletions into account). */
            public int docCount;

            /** True if segment is compound file format. */
            public boolean compound;

            /** Number of files referenced by this segment. */
            public int numFiles;

            /** Net size (MB) of the files referenced by this
             *  segment. */
            public double sizeMB;

            /** Doc store offset, if this segment shares the doc
             *  store files (stored fields and term vectors) with
             *  other segments.  This is -1 if it does not share. */
            public int docStoreOffset = -1;

            /** String of the shared doc store segment, or null if
             *  this segment does not share the doc store files. */
            public String docStoreSegment;

            /** True if the shared doc store files are compound file
             *  format. */
            public boolean docStoreCompoundFile;

            /** True if this segment has pending deletions. */
            public boolean hasDeletions;

            /** Current deletions generation. */
            public long deletionsGen;

            /** Number of deleted documents. */
            public int numDeleted;

            /** True if we were able to open an AtomicReader on this
             *  segment. */
            public boolean openReaderPassed;

            /** Number of fields in this segment. */
            int numFields;

            /** Map that includes certain
             *  debugging details that IndexWriter records into
             *  each segment it creates */
            public Map<String,String> diagnostics;

            /** Status for testing of field norms (null if field norms could not be tested). */
            public CheckIndex.Status.FieldNormStatus fieldNormStatus;

            /** Status for testing of indexed terms (null if indexed terms could not be tested). */
            public CheckIndex.Status.TermIndexStatus termIndexStatus;

            /** Status for testing of stored fields (null if stored fields could not be tested). */
            public CheckIndex.Status.StoredFieldStatus storedFieldStatus;

            /** Status for testing of term vectors (null if term vectors could not be tested). */
            public CheckIndex.Status.TermVectorStatus termVectorStatus;

            /** Status for testing of DocValues (null if DocValues could not be tested). */
            public CheckIndex.Status.DocValuesStatus docValuesStatus;
        }
    }
}

