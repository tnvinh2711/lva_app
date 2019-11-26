package com.lva.shop.callback;

import com.lva.shop.ui.location.model.Address;

public interface FragmentChangedListener {
   default void OnFragmentChangedListener(int tag){}
   default void OnAddressObjChange(int type, Address address){}
   default void OnAddressChange(String address){}
}
