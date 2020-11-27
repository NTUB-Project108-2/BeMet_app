package tw.com.businessmeet.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import tw.com.businessmeet.R;
import tw.com.businessmeet.activity.EditFriendMemoActivity;
import tw.com.businessmeet.adapter.FriendMemoAddColumnRecyclerViewAdapter;
import tw.com.businessmeet.bean.FriendCustomizationBean;
import tw.com.businessmeet.dao.FriendCustomizationDAO;
import tw.com.businessmeet.helper.AsyncTaskHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.service.Impl.FriendCustomizationServiceImpl;

public class EditMemoFragment extends Fragment implements FriendMemoAddColumnRecyclerViewAdapter.ClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private View view;
    // floating button
    private ExtendedFloatingActionButton extendedFloatingActionButton;

    //chip
    private ChipGroup chipGroup;
    private String originalChipContent, updateChipContent, deleteChipContent;
    private String[] originalChipConetentSplit, updateChipContentSplit, deleteChipContentSplit;

    //edit
    private ImageButton editButton;
    private FriendCustomizationDAO friendCustomizationDAO;
    // MemoFragment
    private final FriendCustomizationBean fcb = new FriendCustomizationBean();
    private RecyclerView recyclerViewMemo;
    private List<FriendCustomizationBean> friendCustomizationBeanList = new ArrayList<FriendCustomizationBean>();
    private FriendMemoAddColumnRecyclerViewAdapter friendMemoAddColumnRecyclerViewAdapter;

    // dialog
    private Button confirm, cancel;
    private EditText addColumnMemo, addChipMemo;
    private DBHelper dh = null;
    private FriendCustomizationServiceImpl friendCustomizationServiceImpl = new FriendCustomizationServiceImpl();

    private void openDB() {
        dh = new DBHelper(getContext());
        friendCustomizationDAO = new FriendCustomizationDAO(dh);
    }

    public EditMemoFragment() {
    }

    public static EditMemoFragment newInstance(String param1, String param2) {
        EditMemoFragment fragment = new EditMemoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_memo, container, false);
        FriendCustomizationBean friendCustomizationBean = new FriendCustomizationBean();
        friendCustomizationBean.setFriendNo(getActivity().getIntent().getIntExtra("friendNo", 0));
        friendsearchMemo(friendCustomizationBean);
        System.out.println("friendCustomizationBeanList.size() = " + friendCustomizationBeanList.size());

        // recyclerView
        recyclerViewMemo = (RecyclerView) view.findViewById(R.id.friends_edit_profile_memo_recycleView);

        // floating button
        extendedFloatingActionButton = (ExtendedFloatingActionButton) view.findViewById(R.id.memo_addColumn);
        extendedFloatingActionButton.setOnClickListener(dialogClick);
        openDB();
        return view;
    }

    private void initMemoRecyclerView() {
        // 創建adapter
        friendMemoAddColumnRecyclerViewAdapter = new FriendMemoAddColumnRecyclerViewAdapter(getContext(), friendCustomizationBeanList, this);
        // recycleView設置adapter
        recyclerViewMemo.setAdapter(friendMemoAddColumnRecyclerViewAdapter);
        // 點擊後編輯
        friendMemoAddColumnRecyclerViewAdapter.setClickListener(this::onClick);
        // 設置layoutManager，可以設置顯示效果(線性布局、grid布局、瀑布流布局)
        // 參數:上下文、列表方向(垂直Vertical/水平Horizontal)、是否倒敘
        recyclerViewMemo.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        // 設置item的分割線
        recyclerViewMemo.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }

    public View.OnClickListener dialogClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            originalChipContent = "";
            LayoutInflater inflater = getActivity().getLayoutInflater();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View view = inflater.inflate(R.layout.friend_add_column, null);
            builder.setView(view);
            builder.create();
            AlertDialog alertDialog = builder.show();
            addColumnMemo = (EditText) view.findViewById(R.id.addColumn_dialog_Input);
            addChipMemo = (EditText) view.findViewById(R.id.addTag_dialog_Input);
            chipGroup = (ChipGroup) view.findViewById(R.id.addTag_dialog_selectedBox);
            addChipMemo.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                        originalChipContent = originalChipContent + "," + addChipMemo.getText().toString();
                        System.out.println("originalChipContent = " + originalChipContent);
                        Chip chip = new Chip(getContext());
                        ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(getContext(), null, 0, R.style.Widget_MaterialComponents_Chip_Action);
                        chip.setChipDrawable(chipDrawable);
                        chip.setText(addChipMemo.getText().toString());
                        chip.setCloseIconVisible(true);
                        chip.setOnCloseIconClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                chipGroup.removeView(chip);
                            }
                        });
                        chipGroup.addView(chip);
                        return true;
                    } else if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                        addChipMemo.setText("");
                        return true;
                    }
                    return false;
                }
            });

            confirm = (Button) view.findViewById(R.id.addColumn_dialog_confirmButton);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fcb.setFriendNo(getActivity().getIntent().getIntExtra("friendNo", 0));
                    fcb.setName(addColumnMemo.getText().toString());
                    fcb.setContent(originalChipContent);
                    if (checkData(fcb)) {
                        AsyncTaskHelper.execute(() -> FriendCustomizationServiceImpl.add(fcb), friendCustomizationBean -> {
                            friendCustomizationDAO.add(friendCustomizationBean);
                            friendsearchMemo(friendCustomizationBean);
                        });
                        if (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }

                }
            });

            cancel = (Button) view.findViewById(R.id.addColumn_dialog_cancelButton);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (alertDialog.isShowing()) {
                        alertDialog.dismiss();
                    }
                }
            });
        }
    };

    private boolean checkData(FriendCustomizationBean friendCustomizationBean) {
        if (fcb.getName() == null || fcb.getName().equals("")) { //輸入欄位名稱不可為空
            Toast.makeText(getContext(), "未輸入欄位名稱", Toast.LENGTH_LONG).show();
        } else if ((fcb.getContent() == null || fcb.getContent().equals("")) && (addChipMemo == null || addChipMemo.getText().toString().equals(""))) { //輸入備註和chip皆不可為空
            Toast.makeText(getContext(), "未輸入備註", Toast.LENGTH_LONG).show();
        } else if (addChipMemo.getText().toString().length() >= 1) {
            Toast.makeText(getContext(), "備註輸入完請按Enter確認變成標籤後，才能夠新增備註", Toast.LENGTH_LONG).show();
        } else if (addChipMemo.getText().toString().equals("")) {
            return true;
        } else {
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view, int position) {
        Intent intent = new Intent(getActivity(), EditFriendMemoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("friendCustomizationNo", friendMemoAddColumnRecyclerViewAdapter.getFriendMemo(position).getFriendCustomizationNo());
        bundle.putInt("friendNo", friendMemoAddColumnRecyclerViewAdapter.getFriendMemo(position).getFriendNo());
        bundle.putString("friendId", getActivity().getIntent().getStringExtra("friendId"));
        bundle.putString("name", friendMemoAddColumnRecyclerViewAdapter.getFriendMemo(position).getName());
        bundle.putString("content", friendMemoAddColumnRecyclerViewAdapter.getFriendMemo(position).getContent());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void reloadPage() {
        Intent intent = new Intent(getActivity(), EditMemoFragment.class);
        Bundle bundle = new Bundle();
        bundle.putInt("friendNo", getActivity().getIntent().getIntExtra("friendNo", 0));
        bundle.putString("friendId", getActivity().getIntent().getStringExtra("friendId"));
        startActivity(intent);
    }

    private void friendsearchMemo(FriendCustomizationBean friendNo) {
        AsyncTaskHelper.execute(() -> FriendCustomizationServiceImpl.search(friendNo), fcbl -> {
            for (int i = 0; i < fcbl.size(); i++) {
                if (fcbl.get(i).getFriendNo() == getActivity().getIntent().getIntExtra("friendNo", 0)) {
                    friendCustomizationBeanList.add(fcbl.get(i));
                }
            }
            initMemoRecyclerView();
        });
    }
}
