/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TBA.Sounds;

import java.io.*;
import javax.sound.sampled.*;

/**
 *  The SoundPlayer class loads wav files into memory and plays them back when needed
 *
 * @author cs321tx2
 */
public class SoundResources
{

    boolean isNowMute;

    /** now field is file name of sound that plays back when cell is red */
    String nowLocation = "defaultNow.wav"; //atm this is just windows' chime.wav renamed
    
    //String soonLocation = "defaultSoon.wav";
    String soonLocation = "defaultNow.wav";
    /**
     * actual sound loaded in memory; to play just SoundPlayer.nowClip.start();
     */
    public Clip nowClip;

    /**
     * constructor loads sounds into memory
     */
    public SoundResources()
    {
        isNowMute = false;
        nowClip = Load(nowLocation);
    }

    public void Reload()
    {
        nowClip = Load(nowLocation);
    }

    /**
     *private function called by constructor that loads sound file into memory
     */
    private Clip Load(String filename)
    {
        try
        {
            //open file
            //File file = new File(filename);
            AudioInputStream stream = AudioSystem.getAudioInputStream(SoundResources.class.getResourceAsStream(filename));
            //AudioInputStream stream = AudioSystem.getAudioInputStream(file);
            AudioFormat format = stream.getFormat();

            //we're using clips so that we can just play back sounds from mmemory
            DataLine.Info lineInfo = new DataLine.Info(Clip.class, format);

            //make line
            Clip aClip = (Clip)AudioSystem.getLine(lineInfo);

            //load stream into line
            aClip.open(stream);

            return aClip;
        } catch (Exception ex)
        {
        }

        return null;
    }

    /**
     * This method returns the file location of the sound when meeting is now
     * @return nowLocation
     */
    public String getNowLocation() {
        return nowLocation;
    }

    /**
     * Sets file location of sound when meeting is now
     * @param nowLocation
     */
    public void setNowLocation(String nowLocation) {
        this.nowLocation = nowLocation;
    }

    /**
     * This method plays the file in memory
     */
    public void playNow()
    {
        if(isNowMute == false)
        {
            nowClip.setFramePosition(0);
            nowClip.start();
        }
    }

    /**
     * This method sets the mute flag on or off
     * @param isNowMute
     */
    public void setNowMute(boolean isNowMute) {
        this.isNowMute = isNowMute;
    }

    /**
     * This method gets the mute flag
     * @return isNowMute flag
     */
    public boolean isNowMute() {
        return isNowMute;
    }
      
}
