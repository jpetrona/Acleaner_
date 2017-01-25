package Utilities;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import interfaces.ICallStateListner;

/**
 * Created by Numan on 5/18/2016.
 */
public class MyPhoneStateListner extends PhoneStateListener
{
    private ICallStateListner iCallStateListner;
    public MyPhoneStateListner(ICallStateListner iCallStateListner)
    {
        this.iCallStateListner = iCallStateListner;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        switch (state)
        {
            case TelephonyManager.CALL_STATE_IDLE:
                iCallStateListner.CallStateIdle();
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                iCallStateListner.CallStateOffHook();
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                iCallStateListner.CallStateRinging();
                break;
        }


        super.onCallStateChanged(state, incomingNumber);

    }
}
