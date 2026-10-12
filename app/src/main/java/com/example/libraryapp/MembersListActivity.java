package com.example.libraryapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryapp.adapters.MemberAdapter;
import com.example.libraryapp.database.DatabaseHelper;
import com.example.libraryapp.models.Member;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MembersListActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView rvMembers;
    private MemberAdapter adapter;
    private EditText etSearch;
    private FloatingActionButton fabAddMember;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members_list);

        dbHelper = new DatabaseHelper(this);

        toolbar = findViewById(R.id.toolbarMembersList);
        toolbar.setNavigationOnClickListener(v -> finish());

        etSearch = findViewById(R.id.etSearchMembers);
        rvMembers = findViewById(R.id.rvMembers);
        fabAddMember = findViewById(R.id.fabAddMember);

        rvMembers.setLayoutManager(new LinearLayoutManager(this));

        fabAddMember.setOnClickListener(v -> startActivity(new Intent(this, AddMemberActivity.class)));

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterMembers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMembers();
    }

    private void loadMembers() {
        List<Member> memberList = dbHelper.getAllMembers();
        adapter = new MemberAdapter(this, memberList, member -> {
            Intent intent = new Intent(MembersListActivity.this, MemberDetailsActivity.class);
            intent.putExtra("MEMBER_ID", member.getId());
            startActivity(intent);
        });
        rvMembers.setAdapter(adapter);
    }

    private void filterMembers(String query) {
        if (adapter != null) {
            List<Member> filtered = dbHelper.searchMembers(query);
            adapter.updateList(filtered);
        }
    }
}
