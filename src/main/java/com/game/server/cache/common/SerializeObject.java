package com.game.server.cache.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SerializeObject implements Serializable {
	Object obj = new Object();

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
	
}
