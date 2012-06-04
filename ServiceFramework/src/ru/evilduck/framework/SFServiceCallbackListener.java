package ru.evilduck.framework;

import android.content.Intent;
import android.os.Bundle;

interface SFServiceCallbackListener {

    void onServiceCallback(int requestId, Intent requestIntent, int resultCode, Bundle data);

}