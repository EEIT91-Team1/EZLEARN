package org.ezlearn.model;

public class loginresponse {
	private int error; //1:無帳號 2:密碼錯誤 3:登入成功 

	public int getError() {
		return error;
	}

	public void setError(int error) {
		this.error = error;
	}
	
}
