package pl.training.cleanarchitecturetodo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import pl.training.cleanarchitecturetodo.App;
import pl.training.cleanarchitecturetodo.R;
import pl.training.cleanarchitecturetodo.application.MessageContent;
import pl.training.cleanarchitecturetodo.presentation.MessagesPresenter;

public class WallActivity extends AppCompatActivity implements MessagesPresenter.MessagesUI {

    private ListView postsListView;
    private EditText messageContentView;

    @Inject
    MessagesPresenter messagesPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getComponent(this).inject(this);

        setContentView(R.layout.activity_wall);
        postsListView = (ListView) findViewById(R.id.posts_list);
        messageContentView = (EditText) findViewById(R.id.message_content);

        View sendButton = findViewById(R.id.send_button);
        if (sendButton != null) {
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    messagesPresenter.send(messageContentView.getText().toString());
                    messageContentView.setText(null);
                }
            });
        }

        postsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MessageContent msg = (MessageContent) adapterView.getItemAtPosition(i);
                messagesPresenter.cancel(msg);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        messagesPresenter.attach(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        messagesPresenter.detach();
    }

    @Override
    public void showProgress() {
        //TODO
    }

    @Override
    public void showMessages(List<MessageContent> messages) {
        postsListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messages));
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}
