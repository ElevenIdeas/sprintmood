package net.azurewebsites.sprintmood;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class FeedbackFragment extends Fragment {

	private Feedback mFeedback;
	private TextView mMoodValue = null;
	private SeekBar mMoodSlider = null;
	private EditText mCommentEdit = null;
	
	private RadioGroup mPrivateGroup = null;
	private RadioButton mPrivateRadio = null;
	private RadioButton mPublicRadio = null;

	private Button mSubmitButton = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mFeedback = new Feedback();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//don't call super!
		//super.onCreateView(inflater, container, savedInstanceState); 
		
		//create view to return
		View v = inflater.inflate(R.layout.fragment_feedback, container, false);
		
		// from: http://www.mysamplecode.com/2012/06/android-edittext-text-change-listener.html
		mCommentEdit = (EditText)v.findViewById(R.id.comment_edittext);
		mCommentEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mFeedback.setComment(s.toString());
			}
			
		});
		
			
		// from: http://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
		mCommentEdit.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (v == mCommentEdit) {
					if (!hasFocus) {
						closeKeyboard(getActivity(), mCommentEdit.getWindowToken());
						/*
						InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(
							      Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(mCommentEdit.getWindowToken(), 0);
						*/
					}
				}
				
			}
		});
		
		
		mMoodValue = (TextView)v.findViewById(R.id.mood_value);
		
		// from: http://javatechig.com/android/android-seekbar-example
		mMoodSlider = (SeekBar)v.findViewById(R.id.mood_slider);
		mMoodSlider.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (mMoodValue != null) {
					mFeedback.setScore(progress + 1);
					mMoodValue.setText(Integer.toString(mFeedback.getScore()));
				}
				// from: see function definition below
				closeKeyboard(getActivity(), mCommentEdit.getWindowToken());
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
	
		// from: http://stackoverflow.com/questions/8323778/how-to-set-on-click-listener-on-the-radio-button-in-android
		mPrivateGroup = (RadioGroup)v.findViewById(R.id.private_radiogroup);
		mPrivateGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// from: http://stackoverflow.com/questions/18536195/android-oncheckedchanged-for-radiogroup
				if (checkedId == R.id.private_radio) {
					mFeedback.setIsPrivate(true);
				} else if (checkedId == R.id.public_radio) {
					mFeedback.setIsPrivate(false);
				}
				
				//this only closes keyboard when Checked is Changed, not when same the radio is clicked - fixed with ClickListeners on the RadioButtons
				closeKeyboard(getActivity(), mCommentEdit.getWindowToken());
				
			}
		});

		mPrivateRadio = (RadioButton)v.findViewById(R.id.private_radio);
		mPrivateRadio.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				closeKeyboard(getActivity(), mCommentEdit.getWindowToken());
			}
		});
		
		mPublicRadio = (RadioButton)v.findViewById(R.id.public_radio);
		mPublicRadio.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				closeKeyboard(getActivity(), mCommentEdit.getWindowToken());
			}
		});
		
		mSubmitButton = (Button)v.findViewById(R.id.feedback_submit);
		mSubmitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				closeKeyboard(getActivity(), mCommentEdit.getWindowToken()); //TODO: refactor so params not passed
				SubmitFeedback();				
			}
		});

		return v;
	}
	

	public void SubmitFeedback () {

		String prefApikey = PreferenceManager.getDefaultSharedPreferences(getActivity())
				.getString(getString(R.string.pref_apikey_key), "");
				
		String[] params = new String[8];
		
		params[0] = prefApikey;
		params[1] = Integer.toString(mFeedback.getSprintUserId());
		params[2] = Integer.toString(mFeedback.getScore());  
		params[3] = mFeedback.getComment();
		params[4] = Boolean.toString(mFeedback.isIsPrivate());
		//params[5] = "1";
		//params[6] = "1";
		//params[7] = ""

		SubmitFeedbackTask submitTask = new SubmitFeedbackTask();
		submitTask.execute(params);
		
		return;
	}
	
	public class SubmitFeedbackTask extends AsyncTask<String, Void, String> {
		// from: Sunshine_lesson4a
		
		private final String LOG_TAG = SubmitFeedbackTask.class.getSimpleName();
		
		@Override
		protected String doInBackground(String... params) {

			if (params.length == 0){
				return null;
			}
			
			//from: https://gist.github.com/anonymous/6b306e1f6a21b3718fa4
			
			// These two need to be declared outside the try/catch
			// so that they can be closed in the finally block.
			HttpURLConnection urlConnection = null;
			BufferedReader reader = null;
			 
			// Will contain the raw JSON response as a string.
			String resultJson = null;
			
			String sprintUserId = params[1];
			String score = params[2];
			String comment = params[3];
			String isPrivate = params[4];
			
			try {
			    // Construct the URL
				
			    //URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7");
			 
				final String FORECAST_BASE_URL = "https://sprintmood.azurewebsites.net/api/sprintuser?";
				final String API_PARAM = "apikey";
				final String SPRINTUSERID_PARAM = "sprintuserid";
				final String SCORE_PARAM = "score";
				final String COMMENT_PARAM = "comment";
				final String ISPRIVATE_PARAM = "isprivate";
				final String QUESTIONID_PARAM = "questionid";
				final String ANSWERID_PARAM = "answerid";
				final String APPLIESTOTIME_PARAM = "appliestotime";
				
				
				Uri BuiltUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
						.appendQueryParameter(API_PARAM, params[0])
						.build();
				
				URL url = new URL(BuiltUri.toString());

				//url = new URL("https://sprintmood.azurewebsites.net/api/sprintuser?apikey=2AB5BA6F-ED8F-4E15-AF7A-F76FFB9637C3");
				url = new URL("https://sprintmood.azurewebsites.net/api/sprintuser");
				
				//Log.v(LOG_TAG, "Built URI: " + BuiltUri.toString());
				Log.v(LOG_TAG, "URL: " + url);
				
			    // Create the request to OpenWeatherMap, and open the connection
			    urlConnection = (HttpURLConnection) url.openConnection();
			    //urlConnection.setRequestMethod("POST");
			    /*
			    handle.setRequestMethod("POST");
			    handle.setRequestProperty("Host", Host);
			    handle.setRequestProperty("Content-Type", POSTContentType);
			    handle.setRequestProperty("Content-Length", ""+_urlParameters.getBytes().length);
			    handle.setRequestProperty("User-Agent", UserAgent);
			    handle.setRequestProperty("Accept", Accept);
			    handle.setRequestProperty("Accept-Language", AcceptLang);
			    handle.setRequestProperty("Connection", Connection);
			    */
			    urlConnection.setReadTimeout(10000);
			    urlConnection.setConnectTimeout(15000);
			    urlConnection.setRequestMethod("POST");
			    urlConnection.setDoInput(true);
			    urlConnection.setDoOutput(true);
			    urlConnection.setRequestProperty("apikey", params[0]);
			    /*
			    urlConnection.setRequestProperty("apikey", params[0]);
			    urlConnection.setRequestProperty(COMMENT_PARAM, comment);
			    urlConnection.setRequestProperty(SCORE_PARAM, score);
			    urlConnection.setRequestProperty(ISPRIVATE_PARAM, isPrivate);
			    urlConnection.setRequestProperty(SPRINTUSERID_PARAM, sprintUserId);
			    urlConnection.setRequestProperty(QUESTIONID_PARAM, "1");
			    urlConnection.setRequestProperty(ANSWERID_PARAM, "1");
			    */
			    List<NameValuePair> headerParams = new ArrayList<NameValuePair>();
			    headerParams.add(new BasicNameValuePair(SPRINTUSERID_PARAM, sprintUserId));
			    headerParams.add(new BasicNameValuePair(SCORE_PARAM, score));
			    headerParams.add(new BasicNameValuePair(COMMENT_PARAM, comment));
			    headerParams.add(new BasicNameValuePair(ISPRIVATE_PARAM, isPrivate));
			    headerParams.add(new BasicNameValuePair(QUESTIONID_PARAM, "1"));
			    headerParams.add(new BasicNameValuePair(ANSWERID_PARAM, "1"));
			    //headerParams.add(new BasicNameValuePair(APPLIESTOTIME_PARAM, "18 Sep 2014"));

			    OutputStream os = urlConnection.getOutputStream();
			    BufferedWriter writer = new BufferedWriter(
			            new OutputStreamWriter(os, "UTF-8"));
			    writer.write(getQuery(headerParams));
			    writer.flush();
			    writer.close();
			    os.close();


			    
			    //urlConnection.setRequestProperty("User-Agent", "MyAppAgent");
			    //urlConnection.setRequestProperty("Connection", "Keep-Alive");
			    //urlConnection.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
			    
			    urlConnection.connect();
			 
			    // Read the input stream into a String
			    InputStream inputStream = urlConnection.getInputStream();
			    StringBuffer buffer = new StringBuffer();
			    if (inputStream == null) {
			        // Nothing to do.
			        resultJson = null;
			    }
			    reader = new BufferedReader(new InputStreamReader(inputStream));
			 
			    String line;
			    while ((line = reader.readLine()) != null) {
			        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
			        // But it does make debugging a *lot* easier if you print out the completed
			        // buffer for debugging.
			        buffer.append(line + "\n");
			    }
			 
			    if (buffer.length() == 0) {
			        // Stream was empty.  No point in parsing.
			        resultJson = null;
			    }
			    resultJson = buffer.toString();
			    
			    Log.v(LOG_TAG,"JSON: " + resultJson);
			    
			} 
			catch (IOException e) {
			    Log.e(LOG_TAG, "Error ", e);
			    // If the code didn't successfully get the weather data, there's no point in attemping
			    // to parse it.
			    resultJson = null;
			} 
			finally{
			    if (urlConnection != null) {
			        urlConnection.disconnect();
			    }
			    if (reader != null) {
			        try {
			            reader.close();
			        } 
			        catch (final IOException e) {
			            Log.e(LOG_TAG, "Error closing stream", e);
			        }
			    }
			}
			
			return resultJson;
		}

		private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
		{
		    StringBuilder result = new StringBuilder();
		    boolean first = true;

		    for (NameValuePair pair : params)
		    {
		        if (first)
		            first = false;
		        else
		            result.append("&");

		        result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
		        result.append("=");
		        result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
		    }

		    return result.toString();
		}
		
		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);
		}
	
	}

	// from: http://stackoverflow.com/questions/17213084/how-to-close-soft-keyboard-in-fragment
	public static void closeKeyboard(Context c, IBinder windowToken) {
		//call with: closeKeyboard(getActivity(), yourEditText.getWindowToken());
	    InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
	    mgr.hideSoftInputFromWindow(windowToken, 0);
	}

	
}
