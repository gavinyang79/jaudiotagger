package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.ID3v22Tag;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.TagFieldKey;
import org.jaudiotagger.tag.mp4.Mp4Tag;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * Test Writing to mp4 with top level free data atoms but free atoms and mdat are before ilst so not useful
 */
public class Issue240Test extends AbstractTestCase
{
    public void testWritelargeDataToFile()
    {
        File orig = new File("testdata", "test34.m4a");
        if (!orig.isFile())
        {
            return;
        }

        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test34.m4a");

            AudioFile af = AudioFileIO.read(testFile);
            assertEquals(0,((Mp4Tag)af.getTag()).get(TagFieldKey.COVER_ART).size());

            //Add new image
            RandomAccessFile imageFile = new RandomAccessFile(new File("testdata", "coverart.png"), "r");
            byte[] imagedata = new byte[(int) imageFile.length()];
            imageFile.read(imagedata);
            af.getTag().add(((Mp4Tag) af.getTag()).createArtworkField(imagedata));
            af.commit();

            //Read File back
            af = AudioFileIO.read(testFile);
            assertEquals(1,((Mp4Tag)af.getTag()).get(TagFieldKey.COVER_ART).size());
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }
}