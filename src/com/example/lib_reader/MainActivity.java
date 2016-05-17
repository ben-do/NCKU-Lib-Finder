package com.example.lib_reader;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	ImageView cover;
	TextView name;
	TextView callNum;
	TextView status;
	TextView TV_ISBN;
	ImageButton scanBtn;
	String ISBN;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//cover = (ImageView) findViewById(R.id.cover);
		name = (TextView) findViewById(R.id.name);
		callNum = (TextView) findViewById(R.id.callNum);
		status = (TextView) findViewById(R.id.status);
		scanBtn = (ImageButton) findViewById(R.id.scanBtn);
		
		//�Q��ZxingŪ�����X
		scanBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				try {
					
					Intent intent = new Intent(
							"com.google.zxing.client.android.SCAN");
					intent.putExtra("SCAN_MODE", "QR_CODE_MODE,PRODUCT_MODE");
					startActivityForResult(intent, 0);
				
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "ERROR:" + e, 1).show();

				}

			}
		});
		
		
		
		
		
		
	}
	
/*
	AsyncTask<String, Void, Bitmap> mDownloadTask = new AsyncTask<String, Void, Bitmap>(){
		
		
		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			cover.setImageBitmap(result);
			super.onPostExecute(result);
		}

		protected Bitmap doInBackground(String... urls){
			Bitmap bitmap =null;
			
			try{
				URL url = new URL(urls[0]);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.connect();
				InputStream is = conn.getInputStream();
				bitmap = BitmapFactory.decodeStream(is);
				}catch(MalformedURLException e){
					e.printStackTrace();
				}catch (IOException e){
					e.printStackTrace();
				}
				return bitmap;
		}
	};

	
	mDownloadTask.execute(imageURL);
	*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {

			//�Y���˦��\
			if (resultCode == RESULT_OK) {
				
				ISBN = intent.getStringExtra("SCAN_RESULT");
				
				new AsyncTask<Void, Void, Void>(){
					String Str_name;
					String Str_callNum;
					String Str_status;
					
					protected Void doInBackground(Void... arg0){
						//String url ="http://weblis.lib.ncku.edu.tw/search*cht/i?SEARCH=9789862352397&searchscope=1";
						//�W���O���ե�url
						String url ="http://weblis.lib.ncku.edu.tw/search*cht/i?SEARCH="+ISBN+"&searchscope=1";
						try{
							//�Q��JsoupŪ�����url����l�X
							Document doc = Jsoup.connect(url).get();
						
												
							//�q������l�X�����class��bibInfoData��td���ҡA�A�q�䤤��쬰strong���l���ҡAfirst�N��ҧ�쪺�Ĥ@�ӡC
							Element nameElement = doc.select("td.bibInfoData>strong").first();
							Str_name = nameElement.ownText();//Ū���X�ӱ��X���ݮ��y���W��
							
							//�q������l�X�����class��bibItemsEntry������
							Element element = doc.select(".bibItemsEntry").first();
							Elements elements = element.select("td"); //��elements�s��bibItemsEntry�����Ҧ�td��
							element = elements.get(2);
							Str_callNum = element.ownText();//�ĤG��td�����Ѹ�
							
							element = elements.get(3);
							Str_status = element.ownText();//�ĤT��td�����y���p
							
						}catch(IOException e){
							e.printStackTrace();
							
						}
						
						return null;
					}
					
					@Override
					protected void onPostExecute(Void result) {
						// TODO Auto-generated method stub
						//�NŪ���쪺��Ƴ]�w�������TextView
						name.setText(Str_name);
						callNum.setText(Str_callNum);
						status.setText(Str_status);

						super.onPostExecute(result);
						
						
					}
				}.execute();
			} else if (resultCode == RESULT_CANCELED) {
				callNum.setText("no");
				status.setText("no");
			}
		}
	}

}
