package com.example.desmond.clinc.Model.CallBacks;

/**
 * Created by Desmond on 12/8/2017.
 */

public interface ImageCallback {


    void success(byte[] image);

    void fail();
}
