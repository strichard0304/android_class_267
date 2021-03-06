package com.example.user.simpleui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
//import android.support.v4.app.Fragment; //改成以下
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DrinkOrderDialog.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DrinkOrderDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
//public class DrinkOrderDialog extends Fragment {
public class DrinkOrderDialog extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //UI Component
    NumberPicker mNumberPicker;
    NumberPicker lNumberPicker;
    RadioGroup iceRadioGroup;
    RadioGroup sugarRadioGroup;



    // TODO: Rename and change types of parameters
    //private String mParam1;
    //private String mParam2; //改成以下
    private DrinkOrder drinkOrder;

    private OnFragmentInteractionListener mListener;

    public DrinkOrderDialog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DrinkOrderDialog.
     */
    // TODO: Rename and change types and number of parameters
    //public static DrinkOrderDialog newInstance(String param1, String param2) { //改成以下
    public static DrinkOrderDialog newInstance(DrinkOrder drinkOrder) {
        DrinkOrderDialog fragment = new DrinkOrderDialog();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM1, drinkOrder.getJsonObject().toString());
        //args.putString(ARG_PARAM2, param2);//沒用到
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            //mParam1 = getArguments().getString(ARG_PARAM1);
//            String data = getArguments().getString(ARG_PARAM1);
//            drinkOrder = DrinkOrder.newInstanceWithJsonObject(data);
//            //mParam2 = getArguments().getString(ARG_PARAM2); //沒用到
//        }
//    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_drink_order_dialog, container, false);
//    } //改成以下
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        if (getArguments() != null) {
            String data = getArguments().getString(ARG_PARAM1);
            drinkOrder = DrinkOrder.newInstanceWithJsonObject(data);
        }
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View root = layoutInflater.inflate(R.layout.fragment_drink_order_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(root)
                .setTitle(drinkOrder.drinkName)
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialog, int which) {
                                         drinkOrder.lNumber = lNumberPicker.getValue();
                                         drinkOrder.mNumber = mNumberPicker.getValue();
                                         drinkOrder.ice = getSelectedItemFromRadioGroup(iceRadioGroup);
                                         drinkOrder.sugar = getSelectedItemFromRadioGroup(sugarRadioGroup);
                                         drinkOrder.note = noteEditText.getText().toString();
                                         //if(mListener){
                                         //
                                         //}
                                     }
                                   }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialog, int which) {

                                     }
                                   });
        mNumberPicker = (NumberPicker) root.findViewById(R.id.mNumberPicker);
        mNumberPicker.setMaxValue(100);
        mNumberPicker.setMinValue(0);
        mNumberPicker.setValue(drinkOrder.mNumber);
        lNumberPicker = (NumberPicker) root.findViewById(R.id.lNumberPicker);
        lNumberPicker.setMaxValue(100);
        lNumberPicker.setMinValue(0);
        lNumberPicker.setValue(drinkOrder.lNumber);
        return builder.create();
    }

    // TODO: Rename method, update argument and hook method into UI event
    //public void onButtonPressed(Uri uri) { 不會用到
    //    if (mListener != null) {
    //        mListener.onFragmentInteraction(uri);
    //    }
    //}

    private String getSelectedItemFromRadioGroup(RadioGroup radioGroup){
        int id = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton)radioGroup.findViewById(id);
        return  radioButton.getText().toString();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    //public interface OnFragmentInteractionListener { // 6/20
    public interface OnDrinkOrderListener {
        // TODO: Update argument type and name
        //void onFragmentInteraction(Uri uri); 不會用到
        void OnDrinkOrderFinished(DrinkOrder drinkOrder);
    }
}
