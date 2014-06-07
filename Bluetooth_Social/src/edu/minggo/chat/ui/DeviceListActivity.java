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

import java.util.Set;

import edu.minggo.chat.R;
import edu.minggo.chat.control.BluetoothChatService;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * ���Activity����һ���Ի�����֡������г�ƥ����豸�����������豸
 * ���û�ѡ���б��е��豸��ʱ�򣬸��豸��MAC��ַ�ͻ᷵����Activity
 */
public class DeviceListActivity extends Activity {
    // Debugging
    private static final String TAG = "DeviceListActivity";
    private static final boolean D = true;

    // ����intent���ַ�������
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    private BluetoothAdapter mBtAdapter;  //����������
    private ArrayAdapter<String> mPairedDevicesArrayAdapter; //�����ӹ��������豸����������
    private ArrayAdapter<String> mNewDevicesArrayAdapter;  //̽�⵽�������豸����������

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ����һ������
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.device_list);

        //���÷��ص��¼��Ľ��������ɾ���Ի���Ĳ�����
        setResult(Activity.RESULT_CANCELED);

        // ��ʼ����ť������̽�������豸�ļ�����
        Button scanButton = (Button) findViewById(R.id.button_scan);
        scanButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                doDiscovery();
                v.setVisibility(View.GONE);
            }
        });

        //��ʼ����������Ժͱ�̽�⵽�������豸����������
        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);

        // ��Ժõ������豸View�����ü�����
        ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
        pairedListView.setAdapter(mPairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);

        // ̽��������豸View�����ü�����
        ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);

        // ��̽�������豸��ʱ����androidϵͳע��һ���㲥
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // ��̽���������豸��ʱ����androidϵͳע��һ���㲥
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        //��ȡ����������������
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        //��ȡ��ǰ��ƽ��Ե�����������
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

        // ���������Ե������豸���Ǿ��б���ʾ
        if (pairedDevices.size() > 0) {
            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            String noDevices = getResources().getText(R.string.none_paired).toString();
            mPairedDevicesArrayAdapter.add(noDevices);
        }
        //��ӵ�Activity���������
		BluetoothChatService.allActivity.add(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }

        // ע��������̽��Ĺ㲥
        this.unregisterReceiver(mReceiver);
    }

    /**
     * ̽�������豸
     */
    private void doDiscovery() {
        if (D) Log.d(TAG, "doDiscovery()");

        // ��ʾѭ�����ȣ�androidϵͳ�Դ���
        setProgressBarIndeterminateVisibility(true);
        setTitle(R.string.scanning);
        
        // ��ʾ����̽�⵽�������豸
        findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

        // ���������������̽���Ǿ���ֹͣ̽��Ĺ���
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }

        //��ʼ̽��
        mBtAdapter.startDiscovery();
    }

    // �����豸���б���ļ�����
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // ��ֹͣ̽�⣬��Ϊֻ�����������ʼ��̽��
            mBtAdapter.cancelDiscovery();

            // ��ȡ�豸�����ƺ�����������ʾ�ĳ���
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            // ����һ�����ص��豸Mac��intent
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
           
            setResult(Activity.RESULT_OK, intent);
            
            //�ر�Activity
            finish();
        }
    };

    // ����̽��㲥��������̽�����ʱ������豸�б�
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            //��������豸
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //��intent�л�ȡһ�������豸�Ķ���
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // �����ԵĻ��Ǿͺ���������Ϊ�Ѿ�������Ե��б���
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            // ���豸̽����ɺ��������
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                setTitle(R.string.select_device);
                if (mNewDevicesArrayAdapter.getCount() == 0) {
                    String noDevices = getResources().getText(R.string.none_found).toString();
                    mNewDevicesArrayAdapter.add(noDevices);
                }
            }
        }
    };

}
