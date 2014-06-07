/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.minggo.chat.ui;

import edu.minggo.chat.R;
import edu.minggo.chat.control.BluetoothChatService;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;


/**
 * ���Activity����һ���Ի�����֡������г�ƥ����豸�����������豸
 * ���û�ѡ���б��е��豸��ʱ�򣬸��豸��MAC��ַ�ͻ᷵����Activity
 */
public class DeviceRequestActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ����һ������
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        
        setContentView(R.layout.requestconect);

        //���÷��ص��¼��Ľ��������ɾ���Ի���Ĳ�����
        setResult(Activity.RESULT_CANCELED);

        // ��ʼ����ť������̽�������豸�ļ�����
        Button scanButton = (Button) findViewById(R.id.button_request);
        scanButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                v.setVisibility(View.GONE);
            }
        });
        //��ӵ�Activity���������
		BluetoothChatService.allActivity.add(this);
    }
}
