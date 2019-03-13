// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.nativefiledialogs;

/**
 * A file filter.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class FileFilter
{
    private final String    label;
    private final String [] extensions;


    /**
     * Constructor.
     *
     * @param label The label of the filter
     * @param extensions The file extensions to filter for
     */
    public FileFilter (final String label, final String... extensions)
    {
        this.label = label;
        this.extensions = extensions;
    }


    /**
     * Get the label of the file filter.
     *
     * @return The label
     */
    public String getLabel ()
    {
        return this.label;
    }


    /**
     * Get the file extensions to filter for of the file filter.
     *
     * @return The extensions
     */
    public String [] getExtensions ()
    {
        return this.extensions;
    }
}
