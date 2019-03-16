// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.nativefiledialogs;

import java.io.File;
import java.io.IOException;


/**
 * Little test program, which opens the 3 variations of dialogs and dumps the selection result to
 * the console.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class OpenDialogs
{
    /**
     * The entry function.
     *
     * @param args Not used
     */
    public static void main (final String [] args)
    {
        NativeFileDialogs dlgs;
        try
        {
            dlgs = NativeFileDialogsFactory.create (null);

            File selectFile = dlgs.selectFolder ("Open folder...");
            System.out.println (selectFile);

            selectFile = dlgs.selectFile ("Open file...", new FileFilter ("Shellscript", "sh"));
            System.out.println (selectFile);

            selectFile = dlgs.selectNewFile ("Save...");
            System.out.println (selectFile);
        }
        catch (final PlatformNotSupported | IOException ex)
        {
            ex.printStackTrace ();
        }
    }
}
