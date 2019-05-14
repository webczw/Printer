package com.communication.printer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.communication.printer.domain.ModelVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wilber
 */
public class ModelActivity extends AppCompatActivity{
    private TextView tv1;//item.xml里的TextView：Textviewname
    private TextView tv2;//item.xml里的TextView：Textviewage

    private Button bt;//activity_main.xml里的Button
    private ListView lv;//activity_main.xml里的ListView
    private BaseAdapter adapter;//要实现的类
    private List<ModelVO> modelList = new ArrayList<>();//实体类
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model);
        Intent intent = getIntent();
        String data = intent.getStringExtra("extra_data");

        lv = (ListView) findViewById(R.id.listView1);

        //模拟数据库
        for (int i = 0; i < 50; i++) {
            ModelVO ue = new ModelVO();
            ue.setModelId(Long.parseLong(i+""));
            ue.setModelName(data+" ： 型号"+i);

            modelList.add(ue);
        }


        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return modelList.size();
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = ModelActivity.this.getLayoutInflater();
                View view;

                if (convertView==null) {
                    //因为getView()返回的对象，adapter会自动赋给ListView
                    view = inflater.inflate(R.layout.activity_model_item, null);
                }else{
                    view=convertView;
                    Log.i("info","有缓存，不需要重新生成"+position);
                }
                tv1 = (TextView) view.findViewById(R.id.modelId);
                tv1.setText(modelList.get(position).getModelId().toString());

                tv2 = (TextView) view.findViewById(R.id.modelName);
                tv2.setText(modelList.get(position).getModelName());
                return view;
            }
            @Override
            public long getItemId(int position) {//取在列表中与指定索引对应的行id
                return 0;
            }
            @Override
            public Object getItem(int position) {//获取数据集中与指定索引对应的数据项
                return null;
            }
        };
        lv.setAdapter(adapter);

        //获取当前ListView点击的行数，并且得到该数据
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tv1 = (TextView) view.findViewById(R.id.modelName);//找到Textviewname
                String str = tv1.getText().toString();//得到数据

                Intent intent = new Intent(ModelActivity.this, ImageActivity.class);
                intent.putExtra("extra_data", str);
                //启动Intent
                startActivity(intent);


            }
        });
    }
}
