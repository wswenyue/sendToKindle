package com.wenyue.utils;

import java.io.File;

public class DeletTxt {
	
	
	public static boolean doDelet(String path){
		
		boolean tag = false;
		
		File file = new File( path );
		if( file.exists() ){
			file.delete();
			tag = true;
		}
		
		return tag;
	}

}
