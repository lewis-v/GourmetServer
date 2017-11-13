package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

public class FileUtils {
	/**
     * 复制文件
     * @param fromFile
     * @param toFile
     * @throws IOException 
     */
    public static void copyFile(File fromFile,File toFile) throws IOException{
        FileInputStream ins = new FileInputStream(fromFile);
        FileOutputStream out = new FileOutputStream(toFile);
        byte[] b = new byte[1024];
        int n=0;
        while((n=ins.read(b))!=-1){
            out.write(b, 0, n);
            out.flush();
        }
       
        ins.close();
        out.close();
    }
    /**
     * 将文件转成byte[]
     * @param filename
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(String filename)throws IOException{
    	FileChannel fc = null;
    	try{
    		fc = new RandomAccessFile(filename,"r").getChannel();
    		MappedByteBuffer byteBuffer = fc.map(MapMode.READ_ONLY, 0, fc.size()).load();
    		byte[] result = new byte[(int)fc.size()];
    		if (byteBuffer.remaining()>0){
    			byteBuffer.get(result,0,byteBuffer.remaining());
    		}
    		return result;
    	}catch(IOException e){
    		e.printStackTrace();
    		throw e;
    	}finally{
    		try{
    			fc.close();
    		}catch(IOException e){
    			e.printStackTrace();
    		}
    	}
    }
}
