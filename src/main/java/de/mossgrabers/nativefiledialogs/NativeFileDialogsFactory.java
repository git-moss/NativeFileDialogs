// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2019-2026
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.nativefiledialogs;

import de.mossgrabers.nativefiledialogs.linux.NativeLinuxFileDialogs;
import de.mossgrabers.nativefiledialogs.macos.NativeMacosFileDialogs;
import de.mossgrabers.nativefiledialogs.windows.NativeWindowsFileDialogs;

import com.sun.jna.Platform;

import java.io.File;


/**
 * The factory for creating the platform specific accessor.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class NativeFileDialogsFactory
{
    private static NativeFileDialogs instance;

    /**
     * Constructor.
     */
    private NativeFileDialogsFactory ()
    {
        // Intentionally empty
    }


    /**
     * Creates access to native file dialogs.
     *
     * @param currentDirectory The initial directory to use, may be null
     * @return The instance
     * @throws PlatformNotSupported Thrown if the platform OS is not supported
     */
    public static synchronized NativeFileDialogs create (final File currentDirectory) throws PlatformNotSupported
    {
        if (instance == null)
        {
            switch (Platform.getOSType ())
            {
                case Platform.WINDOWS:
                    instance = new NativeWindowsFileDialogs (currentDirectory, "bitwig");
                    break;

                case Platform.MAC:
                    instance = new NativeMacosFileDialogs (currentDirectory);
                    break;

                case Platform.LINUX:
                    instance = new NativeLinuxFileDialogs (currentDirectory);
                    break;

                default:
                    throw new PlatformNotSupported ();
            }
        }
        return instance;
    }
}
