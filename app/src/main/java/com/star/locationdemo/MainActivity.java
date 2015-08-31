package com.star.locationdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

	private ListView mFunctionListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.function_list);

		mFunctionListView = (ListView) findViewById(R.id.functionList);
		mFunctionListView.setAdapter(new ArrayAdapter<>(this,
				android.R.layout.simple_expandable_list_item_1, getData()));

		mFunctionListView.setOnItemClickListener(

				new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

						Class TargetClass = null;

						switch (position) {
							case 0:
								TargetClass = LocationActivity.class;
								break;

							case 1:
								TargetClass = NotifyActivity.class;
								break;

							case 2:
								TargetClass = QuestActivity.class;
								break;

							default:
								break;
						}

						if (TargetClass != null) {
							Intent intent = new Intent(MainActivity.this, TargetClass);
							startActivity(intent);
						}
					}
				});
	}

	private List<String> getData(){
         
        List<String> data = new ArrayList<>();
        data.add("基础定位功能");
        data.add("位置消息提醒");
        data.add("常见问题说明");
         
        return data;
    }
}
