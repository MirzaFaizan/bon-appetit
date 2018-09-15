package com.apps.fooddelivery;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apps.items.ItemRestaurant;
import com.apps.utils.Methods;

import java.util.Calendar;


public class FragmentInfo extends Fragment {

    private ProgressDialog progressDialog;
    private ItemRestaurant itemRestaurant;
    private TextView textView_name, textView_type, textView_schedule, textView_address;

    public FragmentInfo newInstance(ItemRestaurant itemRestaurant) {
        FragmentInfo fragment = new FragmentInfo();
        Bundle args = new Bundle();
        args.putSerializable("hotel", itemRestaurant);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_info, container, false);

        Methods methods = new Methods(getActivity());
        itemRestaurant = (ItemRestaurant) getArguments().getSerializable("hotel");

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading));

        textView_name = (TextView) v.findViewById(R.id.tv_info_name);
        textView_address = (TextView) v.findViewById(R.id.tv_info_address);
        textView_schedule = (TextView) v.findViewById(R.id.tv_info_schedule);
        textView_type = (TextView) v.findViewById(R.id.tv_info_type);

        textView_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        textView_name.setText(itemRestaurant.getName());
        textView_address.setText(itemRestaurant.getAddress());
        textView_type.setText(itemRestaurant.getCname());
        setOpenTime();

        return v;
    }

    private void setOpenTime() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
//        if(restaurantItem.getOpenTime().size()>(day-1)) {
        switch (day) {
            case Calendar.SUNDAY:
                textView_schedule.setText(getResources().getString(R.string.sunday) + " - " + itemRestaurant.getSunday());
                break;
            case Calendar.MONDAY:
                textView_schedule.setText(getResources().getString(R.string.monday) + " - " + itemRestaurant.getMonday());
                break;
            case Calendar.TUESDAY:
                textView_schedule.setText(getResources().getString(R.string.tuesday) + " - " + itemRestaurant.getTuesday());
                break;
            case Calendar.WEDNESDAY:
                textView_schedule.setText(getResources().getString(R.string.wednesday) + " - " + itemRestaurant.getWednesday());
                break;
            case Calendar.THURSDAY:
                textView_schedule.setText(getResources().getString(R.string.thursday) + " - " + itemRestaurant.getThursday());
                break;
            case Calendar.FRIDAY:
                textView_schedule.setText(getResources().getString(R.string.friday) + " - " + itemRestaurant.getFriday());
                break;
            case Calendar.SATURDAY:
                textView_schedule.setText(getResources().getString(R.string.saturday) + " - " + itemRestaurant.getSaturday());
                break;
        }
//        } else {
//            textView_opentime.setText(getResources().getString(R.string.no_opentime));
//        }
    }

    private void openDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_opentime);

        ViewGroup.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        TextView textView_mon = (TextView) dialog.findViewById(R.id.tv_dialog_monday);
        TextView textView_tue = (TextView) dialog.findViewById(R.id.tv_dialog_tuesday);
        TextView textView_wed = (TextView) dialog.findViewById(R.id.tv_dialog_wednesday);
        TextView textView_thur = (TextView) dialog.findViewById(R.id.tv_dialog_thursday);
        TextView textView_fri = (TextView) dialog.findViewById(R.id.tv_dialog_friday);
        TextView textView_sat = (TextView) dialog.findViewById(R.id.tv_dialog_saturday);
        TextView textView_sun = (TextView) dialog.findViewById(R.id.tv_dialog_sunday);

        textView_sun.setText(getResources().getString(R.string.sunday) + " - " + itemRestaurant.getSunday());
        textView_mon.setText(getResources().getString(R.string.monday) + " - " + itemRestaurant.getMonday());
        textView_tue.setText(getResources().getString(R.string.tuesday) + " - " + itemRestaurant.getTuesday());
        textView_wed.setText(getResources().getString(R.string.wednesday) + " - " + itemRestaurant.getWednesday());
        textView_thur.setText(getResources().getString(R.string.thursday) + " - " + itemRestaurant.getThursday());
        textView_fri.setText(getResources().getString(R.string.friday) + " - " + itemRestaurant.getFriday());
        textView_sat.setText(getResources().getString(R.string.saturday) + " - " + itemRestaurant.getSaturday());

        dialog.show();
    }
}
