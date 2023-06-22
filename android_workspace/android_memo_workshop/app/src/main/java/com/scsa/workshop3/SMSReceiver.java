package com.scsa.workshop3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //문자메시지에서 내용 추출하여 toast한다.
        Bundle bundle = intent.getExtras();
        Object[] messages = (Object[]) bundle.get("pdus");
        SmsMessage[] smsMessage = new SmsMessage[messages.length];

        int cursor = 0;
        for (Object message : messages) {
            smsMessage[cursor++] = SmsMessage.createFromPdu((byte[]) message);
        }

        //문자 내용 toast로 보여주기
        Toast.makeText(context,
                "SMSReceiver::" + smsMessage[0].getMessageBody(),
                Toast.LENGTH_SHORT).show();

        //문자 받으면 문자 내용을 sms로 받아서 MainActivity로 이동
        Intent intent1 = new Intent(context, MainActivity.class);
        intent1.putExtra("sms", smsMessage[0].getMessageBody());
        context.startActivity(intent1);

    }
}
