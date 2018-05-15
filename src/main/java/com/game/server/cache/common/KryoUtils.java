package com.game.server.cache.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.google.protobuf.ByteString.Output;

/**
 * Kryo序列化工具类
 * @author zhenym
 * @date 2015-4-15
 */
public class KryoUtils{
	
	/**
	 * 序列化
	 * @param <T>
	 * @param object
	 * @return
	 */
	public static <T> byte[] toBytes(T object){
		if(object == null){
			return null;
		}
		
		Kryo kryo = new Kryo();
		ByteArrayOutputStream out = null;
		Output output = null;
		try{
			/*out = new ByteArrayOutputStream();
			output = new Output(out);
			
			kryo.write
			output.close();*/
			return out.toByteArray();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(out != null){
				try{
					out.close();
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
			
			if(output != null){
				/*output.close();*/
			}
		}
		
		return null;
	}
	
	/**
	 * 序列化对象为字符串
	 * @param <T>
	 * @param object
	 * @return
	 */
	public static <T> String toString(T object){
		if(object == null){
			return null;
		}
		
		Kryo kryo = new Kryo();
		ByteArrayOutputStream out = null;
		Output output = null;
		try{
			out = new ByteArrayOutputStream();
			/*output = new Output(out);
			
			kryo.writeClassAndObject(output, object);*/
			output.close();
			return out.toString("ISO-8859-1");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(out != null){
				try{
					out.close();
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
			
			if(output != null){
				/*output.close();*/
			}
		}
		
		return null;
	}
	
	/**
	 * 反序列化
	 * @param <T>
	 * @param bytes
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T fromBytes(byte[] bytes){
		if(bytes == null){
			return null;
		}
		
		Kryo kryo = new Kryo();
		InputStream in = null;
		Input input = null;
		
		try{
			in = new ByteArrayInputStream(bytes);
			input = new Input(in);
			
			return (T)kryo.readClassAndObject(input);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(in != null){
				try{
					in.close();
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
			
			if(input != null){
				input.close();
			}
		}
		
		return null;
	}
}