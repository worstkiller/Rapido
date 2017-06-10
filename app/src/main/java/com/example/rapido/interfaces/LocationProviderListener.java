package com.example.rapido.interfaces;

import java.util.List;

/**
 * Created by Khushhal on 6/9/2017.
 */

public interface LocationProviderListener {
    void onLocationAvailable(List<String> suggestions);

    void onLocationNotAvailable();
}
