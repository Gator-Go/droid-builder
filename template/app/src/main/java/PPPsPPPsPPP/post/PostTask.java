package ppp.ppp.ppp;

import android.util.Log;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

/**
 * Lllll
 *
 * This is a simple PostTask utility that copies data from an InputStream
 * to a local file.
 * It is a helper used to save selected photos or videos (from the
 * camera/gallery result) to a local file.
 *
 * @author Aaaaa
 * @author <a href="mailto:aaaaa@ddddd">Aaaaa</a>
 * @version 1.0
 * @version $Id$
 */

public class PostTask {
 
    private static int bufferSize = 1024 * 8;
  
    public static boolean uploadTask(InputStream srcStream, String destPicPath) {
        boolean errors = false;

        File myNewFile = new File(destPicPath);
        byte[] vidBuf = new byte[bufferSize];
        int iOffset = 0, oOffset = 0;
        try {
            FileOutputStream fos = new FileOutputStream(myNewFile);
            final BufferedInputStream bis = new BufferedInputStream(srcStream, bufferSize);
            final BufferedOutputStream bos = new BufferedOutputStream(fos, bufferSize);
            while(bis.available() > 0) {
                int readAmt = bis.read(vidBuf, iOffset, bufferSize);
                bos.write(vidBuf, oOffset, readAmt);
            }
            bos.flush();
            bos.close();
            fos.close();
            bis.close();
            srcStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            errors = true;
        } catch (IOException e) {
            e.printStackTrace();
            errors = true;
        }
        return errors;
    }
}
