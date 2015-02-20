package net.azurewebsites.sprintmood;

public class Feedback {

	private int mSprintUserId;
	private int mScore;
	private String mComment;
	private boolean mIsPrivate;
	
	public Feedback () {
		mSprintUserId = 3037;
		mScore = 5;
		mComment = "";
		mIsPrivate = false;
		
	}

	public int getSprintUserId() {
		return mSprintUserId;
	}

	public void setSprintUserId(int sprintUserId) {
		mSprintUserId = sprintUserId;
	}

	public int getScore() {
		return mScore;
	}

	public void setScore(int score) {
		mScore = score;
	}

	public String getComment() {
		return mComment;
	}

	public void setComment(String comment) {
		mComment = comment;
	}

	public boolean isIsPrivate() {
		return mIsPrivate;
	}

	public void setIsPrivate(boolean isPrivate) {
		mIsPrivate = isPrivate;
	}
	
	
	
}
