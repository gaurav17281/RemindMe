package com.calenderEvent.Database;

public class Reminder {
	private static int mPriority;
	private static String mId;
	private static String mTitle;
	private static String mSummary;
	private static String mDetails;
	private static String mStartTime;
	private static String mEndTime;
	private static String mContactInfo;
	private static int mState;

	/**
	 * @return the mPriority
	 */
	public static int getPriority() {
		return mPriority;
	}

	/**
	 * @param mPriority
	 *            the mPriority to set
	 */
	public static void setPriority(int mPriority) {
		Reminder.mPriority = mPriority;
	}

	/**
	 * @return the mId
	 */
	public static String getId() {
		return mId;
	}

	/**
	 * @param mId
	 *            the mId to set
	 */
	public static void setId(String mId) {
		Reminder.mId = mId;
	}

	/**
	 * @return the mTitle
	 */
	public static String getTitle() {
		return mTitle;
	}

	/**
	 * @param mTitle
	 *            the mTitle to set
	 */
	public static void setTitle(String mTitle) {
		Reminder.mTitle = mTitle;
	}

	/**
	 * @return the mSummary
	 */
	public static String getSummary() {
		return mSummary;
	}

	/**
	 * @param mSummary
	 *            the mSummary to set
	 */
	public static void setSummary(String mSummary) {
		Reminder.mSummary = mSummary;
	}

	/**
	 * @return the mDetails
	 */
	public static String getDetails() {
		return mDetails;
	}

	/**
	 * @param mDetails
	 *            the mDetails to set
	 */
	public static void setDetails(String mDetails) {
		Reminder.mDetails = mDetails;
	}

	/**
	 * @return the mStartTime
	 */
	public static String getStartTime() {
		return mStartTime;
	}

	/**
	 * @param mStartTime
	 *            the mStartTime to set
	 */
	public static void setStartTime(String mStartTime) {
		Reminder.mStartTime = mStartTime;
	}

	/**
	 * @return the mEndTime
	 */
	public static String getEndTime() {
		return mEndTime;
	}

	/**
	 * @param mEndTime
	 *            the mEndTime to set
	 */
	public static void setEndTime(String mEndTime) {
		Reminder.mEndTime = mEndTime;
	}

	/**
	 * @return the mContactInfo
	 */
	public static String getContactInfo() {
		return mContactInfo;
	}

	/**
	 * @param mContactInfo
	 *            the mContactInfo to set
	 */
	public static void setContactInfo(String mContactInfo) {
		Reminder.mContactInfo = mContactInfo;
	}

	/**
	 * @return the mState
	 */
	public static int getState() {
		return mState;
	}

	/**
	 * @param mState
	 *            the mState to set
	 */
	public static void setState(int mState) {
		Reminder.mState = mState;
	}
}
