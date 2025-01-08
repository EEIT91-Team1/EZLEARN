package org.ezlearn.DTO;

public class LoginResponse {
	
	private int error; //1:無帳號 2:密碼錯誤 3:登入成功
	private String msg;

	public int getError() {
		return error;
	}

	public void setError(int error) {
		this.error = error;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
