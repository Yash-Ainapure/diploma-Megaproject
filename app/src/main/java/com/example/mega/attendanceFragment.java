package com.example.mega;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link attendanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressLint("ParcelCreator")
public class attendanceFragment extends Fragment{
    Calendar c;
    int absentDaysCounter=0;
    private DatabaseReference myRef;
    List<Object> li1=new ArrayList<>();
    ListView lv;
    Button btnnxt,btnprv;
    TextView uname,uattend,uabsnt,upercent;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public attendanceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment attendanceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static attendanceFragment newInstance(String param1, String param2) {
        attendanceFragment fragment = new attendanceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("attendance").child("user1");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        li1.add((snapshot1.getValue()).toString());
                        absentDaysCounter++;
                    }
                    setdapter();
                    if(absentDaysCounter!=0){loadData();}
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "database error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadData() {
        SessionManagement sessionManagement=new SessionManagement(getActivity());
        String UserId=sessionManagement.getSession();
        uname.append(UserId);

        c = Calendar.getInstance();
        upercent.append(String.format("%.2f", ((c.getActualMaximum(Calendar.DAY_OF_MONTH)-absentDaysCounter*1.0)/(c.getActualMaximum(Calendar.DAY_OF_MONTH))*100))+"%");

        uabsnt.append(absentDaysCounter+"");

        uattend.append(absentDaysCounter+"/"+c.getActualMaximum(Calendar.DAY_OF_MONTH));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_attendance, container, false);

        uname=(TextView) v.findViewById(R.id.username);
        uattend=(TextView) v.findViewById(R.id.attendance);
        uabsnt=(TextView) v.findViewById(R.id.absent);
        upercent=(TextView) v.findViewById(R.id.percentage);
        btnprv=(Button) v.findViewById(R.id.btnprv);
        btnnxt=(Button) v.findViewById(R.id.btnnxt);
        lv=(ListView) v.findViewById(R.id.listview);

        btnprv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btnnxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "hello next", Toast.LENGTH_SHORT).show();
                upercent.append(absentDaysCounter+"");

            }
        });
        return  v;
    }
    public void setdapter(){
        ArrayAdapter adapter=new ArrayAdapter(getContext(),R.layout.listviewer,R.id.textviewxy,li1);
        lv.setAdapter(adapter);
    }
}