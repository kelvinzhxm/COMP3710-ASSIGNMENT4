package 
COMP 3710;
XZZ0059@auburn.edu;
Assignment 4;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    TextView balance;
    EditText editDate;
    EditText editPrice;
    EditText editItem;
    Button btnAdd;
    Button btnSub;
    TextView history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);

        balance = (TextView) findViewById(R.id.balance);
        editDate = (EditText) findViewById(R.id.editDate);
        editPrice = (EditText) findViewById(R.id.editPrice);
        editItem = (EditText) findViewById(R.id.editItem);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnSub = (Button) findViewById(R.id.btnSub);
        history = (TextView) findViewById(R.id.historyContent);
        AddTransaction();
        GetHistory();
    }

    public void AddTransaction(){
        btnAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        double price = Double.parseDouble(editPrice.getText().toString());
                        boolean result = myDb.createTransaction(editItem.getText().toString(), editDate.getText().toString(), price);
                        if (result)
                            Toast.makeText(MainActivity.this, "Transaction Created", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MainActivity.this, "Transaction Not Created", Toast.LENGTH_LONG).show();
                        GetHistory();
                        ClearText();
                    }
                }
        );

        btnSub.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        double price = -1 * Double.parseDouble(editPrice.getText().toString());
                        boolean result = myDb.createTransaction(editItem.getText().toString(), editDate.getText().toString(), price);
                        if (result)
                            Toast.makeText(MainActivity.this, "Transaction Created", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MainActivity.this, "Transaction Not Created", Toast.LENGTH_LONG).show();
                        GetHistory();
                        ClearText();
                    }
                }
        );
    }

    public void GetHistory(){
        Cursor result = myDb.getAllData();
        StringBuffer buffer = new StringBuffer();
        Double balance = 0.0;
        while(result.moveToNext()){
            // get price string to remove negative sign
            String priceString = result.getString(3);
            double price = Double.parseDouble(result.getString(3));
            balance += price;
            if (price < 0) {
                buffer.append("Spent $");
                priceString = priceString.substring(1);
            }
            else { buffer.append("Added $");}
            buffer.append(priceString + " on " + result.getString(2) +
                    " for " + result.getString(1) + "\n");
        }
        MainActivity.this.balance.setText("Current Balance: $" + Double.toString(balance));
        MainActivity.this.history.setText(buffer);
    }

    public void ClearText(){
        MainActivity.this.editDate.setText("");
        MainActivity.this.editPrice.setText("");
        MainActivity.this.editItem.setText("");
    }
}
