/*
 * $RCSfile: FileLoadDescriptor.java,v $
 *
 * Copyright (c) 2005 Sun Microsystems, Inc. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * $Revision: 1.2 $
 * $Date: 2005-11-15 00:38:14 $
 * $State: Exp $
 */
package javax.media.jai.operator;
import com.sun.media.jai.codec.ImageDecodeParam;
import java.awt.RenderingHints;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.InputStream;
import javax.media.jai.JAI;
import javax.media.jai.OperationDescriptorImpl;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.RenderedOp;
import javax.media.jai.registry.RenderedRegistryMode;

/**
 * An <code>OperationDescriptor</code> describing the "FileLoad" operation.
 *
 * <p>In the default instance the <code>validateParameters()</code> method
 * checks that the named file exists and is readable.  If not, it will return
 * <code>false</code>, causing <code>JAI.createNS()</code> to throw an
 * <code>IllegalArgumentException</code>.
 *
 * In special cases like when an image is loaded from a Remote system, 
 * the above check for existence of a file on the local system could be bypassed.
 * This is done by setting the <code>Boolean</code> variable <code>checkFileLocally</code> to <code>FALSE</code> in the <code>ParameterBlock</code>
 * 
 * <p> The allowable formats are those registered with the
 * <code>com.sun.media.jai.codec.ImageCodec</code> class.
 *
 * <p> The second parameter contains an instance of
 * <code>ImageDecodeParam</code> to be used during the decoding.
 * It may be set to <code>null</code> in order to perform default
 * decoding, or equivalently may be omitted.
 *
 * <p><b> The classes in the <code>com.sun.media.jai.codec</code>
 * package are not a committed part of the JAI API.  Future releases
 * of JAI will make use of new classes in their place.  This
 * class will change accordingly.</b>
 * 
 * <p><table border=1>
 * <caption>Resource List</caption>
 * <tr><th>Name</th>        <th>Value</th></tr>
 * <tr><td>GlobalName</td>  <td>fileload</td></tr>
 * <tr><td>LocalName</td>   <td>fileload</td></tr>
 * <tr><td>Vendor</td>      <td>com.sun.media.jai</td></tr>
 * <tr><td>Description</td> <td>Reads an image from a file.</td></tr>
 * <tr><td>DocURL</td>      <td>http://java.sun.com/products/java-media/jai/forDevelopers/jai-apidocs/javax/media/jai/operator/FileLoadDescriptor.html</td></tr>
 * <tr><td>Version</td>     <td>1.0</td></tr>
 * <tr><td>arg0Desc</td>    <td>The path of the file to read from.</td></tr>
 * <tr><td>arg1Desc</td>    <td>The ImageDecodeParam to use.</td></tr>
 * <tr><td>arg2Desc</td>    <td>Boolean specifying if File existence should be checked locally.</td></tr>
 * </table></p>
 *
 * <p><table border=1>
 * <caption>Parameter List</caption>
 * <tr><th>Name</th>          <th>Class Type</th>
 *                            <th>Default Value</th></tr>
 * <tr><td>filename</td>      <td>java.lang.String</td>
 *                            <td>NO_PARAMETER_DEFAULT</td>
 * <tr><td>param</td>         <td>com.sun.media.jai.codec.ImageDecodeParam</td>
 *                            <td>null</td>
 * <tr><td>checkFileLocally</td> <td>java.lang.Boolean</td>
 *                            <td>TRUE</td>
 * </table></p>
 *
 * @see javax.media.jai.OperationDescriptor
 */
public class FileLoadDescriptor extends OperationDescriptorImpl {

    /**
     * The resource strings that provide the general documentation and
     * specify the parameter list for the "FileLoad" operation.
     */
    private static final String[][] resources = {
        {"GlobalName",  "FileLoad"},
        {"LocalName",   "FileLoad"},
        {"Vendor",      "com.sun.media.jai"},
        {"Description", JaiI18N.getString("FileLoadDescriptor0")},
        {"DocURL",      "http://java.sun.com/products/java-media/jai/forDevelopers/jai-apidocs/javax/media/jai/operator/FileLoadDescriptor.html"},
        {"Version",     JaiI18N.getString("DescriptorVersion")},
        {"arg0Desc",    JaiI18N.getString("FileLoadDescriptor1")},
        {"arg1Desc",    JaiI18N.getString("FileLoadDescriptor4")},
	{"arg2Desc",    JaiI18N.getString("FileLoadDescriptor5")}
    };

    /** The parameter names for the "FileLoad" operation. */
    private static final String[] paramNames = {
        "filename", "param", "checkFileLocally"
    };

    /** The parameter class types for the "FileLoad" operation. */
    private static final Class[] paramClasses = {
        java.lang.String.class,
        com.sun.media.jai.codec.ImageDecodeParam.class,
	java.lang.Boolean.class
    };

    /** The parameter default values for the "FileLoad" operation. */
    private static final Object[] paramDefaults = {
        NO_PARAMETER_DEFAULT, null, Boolean.TRUE
    };

    /** Constructor. */
    public FileLoadDescriptor() {
        super(resources, 0, paramClasses, paramNames, paramDefaults);
    }

    /**
     * Validates the input parameters.
     *
     * <p> In addition to the standard checks performed by the
     * superclass method, this method by default checks that the source file
     * exists and is readable. This check may be bypassed by setting the
     * <code>checkFileLocally</code> parameter to <code>FALSE</code>
     */
    protected boolean validateParameters(ParameterBlock args,
                                         StringBuffer msg) {
        if (!super.validateParameters(args, msg)) {
            return false;
        }

	Boolean checkFile = (Boolean)args.getObjectParameter(2);
	if (checkFile.booleanValue()){
	  String filename = (String)args.getObjectParameter(0);
	  File f = new File(filename);
	  boolean fileExists = f.exists();
	  if (!fileExists) {
	      // Check if the file is accessible as an InputStream resource.
	      // This would be the case if the application and the image file
	      // are packaged in a JAR file
	      InputStream is = getClass().getResourceAsStream(filename);
	      if(is == null) {
		  msg.append("\"" + filename + "\": " + 
			     JaiI18N.getString("FileLoadDescriptor2"));
		  return false;
	      }
	  } else { // file exists
	      if (!f.canRead()) {
		  msg.append("\"" + filename + "\": " + 
                       JaiI18N.getString("FileLoadDescriptor3"));
		  return false;
	      }
	  }
	}
        return true;
    }


    /**
     * Reads an image from a file.
     *
     * <p>Creates a <code>ParameterBlockJAI</code> from all
     * supplied arguments except <code>hints</code> and invokes
     * {@link JAI#create(String,ParameterBlock,RenderingHints)}.
     *
     * @see JAI
     * @see ParameterBlockJAI
     * @see RenderedOp
     *
     * @param filename The path of the file to read from.
     * @param param The ImageDecodeParam to use.
     * May be <code>null</code>.
     * @param checkFileLocally Boolean specifying if File existence should be checked locally
     * May be <code>null</code>.
     * @param hints The <code>RenderingHints</code> to use.
     * May be <code>null</code>.
     * @return The <code>RenderedOp</code> destination.
     * @throws IllegalArgumentException if <code>filename</code> is <code>null</code>.
     */
    public static RenderedOp create(String filename,
                                    ImageDecodeParam param,
                                    Boolean checkFileLocally,
                                    RenderingHints hints)  {
        ParameterBlockJAI pb =
            new ParameterBlockJAI("FileLoad",
                                  RenderedRegistryMode.MODE_NAME);

        pb.setParameter("filename", filename);
        pb.setParameter("param", param);
        pb.setParameter("checkFileLocally", checkFileLocally);

        return JAI.create("FileLoad", pb, hints);
    }
}
