package com.example.dmitryvedmed.taskbook.ui;

import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dmitryvedmed.taskbook.R;
import com.example.dmitryvedmed.taskbook.helper.ItemTouchHelperAdapter;
import com.example.dmitryvedmed.taskbook.helper.ItemTouchHelperViewHolder;
import com.example.dmitryvedmed.taskbook.logic.ListTask;
import com.example.dmitryvedmed.taskbook.untils.SingletonFonts;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class ListTaskRecyclerAdapter extends RecyclerView.Adapter<ListTaskRecyclerAdapter.RecyclerViewHolder>
        implements ItemTouchHelperAdapter {

    private Context context;
    private ListTask listTask;
    private boolean onBind;
    private List<EditText> editTexts;
    private int hasInsertInside = -1;
    private ListTaskActivity activity;
    private int fromPos, toPos;

    public ListTaskRecyclerAdapter(ListTask listTask, Context context) {
        this.context = context;
        activity = (ListTaskActivity)context;
        this.listTask = listTask;
        if(listTask.getId() == -1)
            listTask.getUncheckedTasks().add("");
        editTexts = new ArrayList<>();
    }

    public ListTask getListTask() {
        return listTask;
    }

    @Override
    public void onItemMove(final int fromPosition, final int toPosition) {
        Log.d("TAG", " FROM P - " + fromPosition + " TO P - " + toPosition);
        activity.setItemMovement(false);
        fromPos = fromPosition;
        toPos = toPosition;
        if(fromPosition>=listTask.getUncheckedTasks().size()) {
            int fromP = fromPosition - (listTask.getUncheckedTasks().size()+1);
            int toP = toPosition - (listTask.getUncheckedTasks().size()+1);
            fromPos = fromP;
            toPos = toP;
            Log.d("TAG", " NEW       FROM P - " + fromPosition + " TO P - " + toPosition);
            if(toP<0)
                return;
            String prev = listTask.getCheckedTasks().remove(fromP);
            listTask.getCheckedTasks().add(toP, prev);
            notifyItemMoved(fromPosition, toPosition);
            notifyItemChanged(fromPosition);
            notifyItemChanged(toPosition);
            return;
        }
        if(toPosition>=listTask.getUncheckedTasks().size()) {
            return;
        }
        String prev = listTask.getUncheckedTasks().remove(fromPosition);
        listTask.getUncheckedTasks().add(toPosition, prev);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {

    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder, View.OnTouchListener {
        private EditText editText;
        private EditTextListener editTextListener;
        private CheckBoxListener checkBoxListener;
        private Button button;
        private ImageView imageView;
        private CheckBox checkBox;
        private TextView newPoint;
        private View deliver;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            System.out.println("ViewHolder constructor");
            System.out.println("TYPE = " + this.getItemViewType());
            ///????
            editTextListener = new EditTextListener();
            checkBoxListener = new CheckBoxListener();

            deliver =  itemView.findViewById(R.id.deliver_rec);
            if(deliver == null)
                Log.d("TAG", "      DELIVER NUUUUUL" );
            else{
                Log.d("TAG", "      DELIVER NOT NUUUUUL" );
            }
            switch (getItemViewType()){
                case 0:
                    break;
                case 1:
                    break;
            }

            editText = (EditText) itemView.findViewById(R.id.itemListEditText);
            if(editText!=null) {
                editText.setTypeface(SingletonFonts.getInstance(context).getRobotoRegular());
                editText.addTextChangedListener(editTextListener);
            }

            if(editText != null) {
               /* editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if( keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER
                                && keyEvent.getAction()==KeyEvent.ACTION_DOWN){
                            listTask.getUncheckedTasks().add("");
                            //update();
                            notifyItemChanged(listTask.getUncheckedTasks().size()-1);
                            return true;
                        }
                        return false;
                    }
                });*/
                //editText.requestFocus();
            }


            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
            if(checkBox != null) {
                checkBox.setOnCheckedChangeListener(checkBoxListener);
            }


            button = (Button) itemView.findViewById(R.id.delButton);


            imageView = (ImageView) itemView.findViewById(R.id.drag);
            if(imageView!=null)
                imageView.setOnTouchListener(this);

            newPoint = (TextView)itemView.findViewById(R.id.newPoint);
            if(newPoint!=null)
                newPoint.setTypeface(SingletonFonts.getInstance(context).getRobotoRegular());
        }

        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {
            Log.d("TAG", "      CLEAR" );
            notifyItemChanged(fromPos);
            notifyItemChanged(toPos);
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Log.d("TAG", "      ON TOUCH " + motionEvent.getAction() );
            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    Log.d("TAG", "      DOWN" );
                    activity.setItemMovement(true);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    Log.d("TAG", "      UP" );
                    //update();
                    break;
            }
            return false;
        }
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerViewHolder recyclerViewHolder = null;
        switch (viewType)
        {
            case 0:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_task_activity, parent,false);
                recyclerViewHolder = new RecyclerViewHolder(view);
                break;
            case 1:
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_list_task_button, parent,false);
                recyclerViewHolder = new RecyclerViewHolder(view1);
                view1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("TAG", "click!!!!!!!!!!!!!!!!!" );

                        listTask.getUncheckedTasks().add("");
                        Log.d("TAG", "Edit texts size = " + editTexts.size() );
                        requestFocusLast();
                        activity.scroll(listTask.getUncheckedTasks().size()-1);
                        update();
                        requestFocusTo(listTask.getUncheckedTasks().size()-1);
                    }
                });
                break;
        }
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        //Log.d("TAG", "ON BIND VIEW HOLDER" );
        String type = holder.getItemViewType() == 0 ? "editText":"button";
        Log.d("TAG", "POSITION -" + position + "," +" TYPE -" +  type );

        switch (holder.getItemViewType()){
            case 0:
                if(position < listTask.getUncheckedTasks().size()) {
                    holder.editTextListener.updatePosition(holder.getAdapterPosition());
                    holder.editText.setText(listTask.getUncheckedTasks().get(position));
                    holder.checkBoxListener.updatePosition(position);

                    onBind = true;
                    holder.checkBox.setChecked(false);
                    onBind = false;

                    // holder.editText.setMovementMethod();

                    holder.editText.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
                    holder.editText.setAlpha(1f);

                    holder.editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View view, boolean b) {
                            Log.d("TAG", "ET " + position + " FOCUS " + b );
                            if(b) {
                                holder.button.setVisibility(VISIBLE);
                                holder.editText.setSelection(holder.editText.getText().length());
                            }
                            else {
                                holder.button.setVisibility(View.INVISIBLE);
                            }
                        }
                    });

        /*    if(position==listTask.getUncheckedTasks().size()-1) {

              //  Log.d("TAG", "ET requestFocus for " + position  );
        //        holder.editText.requestFocus();
                holder.button.setVisibility(View.VISIBLE);
            } else {
                holder.button.setVisibility(View.INVISIBLE);
            }
*/
                    holder.button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listTask.getUncheckedTasks().remove(position);
                            editTexts.remove(position);
                            Log.d("TAG", "Edit texts remove = " + position );
                            Log.d("TAG", "Edit texts size = " + editTexts.size() );
                            update();
                            if(position==0)
                                requestFocusTo(position);
                            else
                                requestFocusTo(position-1);
                            //notifyItemChanged(position);
                        }
                    });

                    if(editTexts.size()==position) {
                        editTexts.add(position, holder.editText);
                        Log.d("TAG", "Edit texts add = " + position );
                    }
                    else {
                        editTexts.remove(position);
                        editTexts.add(position,holder.editText);
                        Log.d("TAG", "Edit texts remove and add = " + position );
                    }
                    Log.d("TAG", "Edit texts size = " + editTexts.size() );
                }

                if(holder.editText!=null) {
                    holder.editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                            if( keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER
                                    && keyEvent.getAction()==KeyEvent.ACTION_DOWN){
                                if(listTask.getUncheckedTasks().size() < position)
                                    return true;
                                Log.d("TAG", "click ENTER Add " + position );
                                listTask.getUncheckedTasks().add(position + 1, "");
                                update();
                                //notifyItemChanged(position+1);
                                requestFocusTo(position+1);
                                return true;
                            }
                            return false;
                        }
                    });

                   holder.editText.setOnKeyListener(new View.OnKeyListener()
                    {
                        public boolean onKey(View v, int keyCode, KeyEvent event)
                        {
                            if(event.getAction() == KeyEvent.ACTION_DOWN &&
                                    (keyCode == KeyEvent.KEYCODE_DEL))
                            {
                                if(listTask.getUncheckedTasks().size() < position)
                                    return false;
                                if(listTask.getUncheckedTasks().get(position).length()==0){
                                    listTask.getUncheckedTasks().remove(position);
                                    //notifyItemRemoved(position);
                                    update();
                                    requestFocusTo(position-1);
                                    return true;
                                }
                            }
                            return false;
                        }
                    }
                    );
                }

                if(position > listTask.getUncheckedTasks().size())
                {
                    holder.editTextListener.updatePosition(holder.getAdapterPosition());
                    String s = (listTask.getCheckedTasks().get(position - (listTask.getUncheckedTasks().size()+1)));
                    holder.editText.setText(s);
                    holder.editText.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.editText.setAlpha(0.5f);
                    holder.editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View view, boolean b) {
                            if(b)
                                holder.button.setVisibility(VISIBLE);
                            else
                                holder.button.setVisibility(View.INVISIBLE);
                        }
                    });


                    holder.button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d("TAG", " BUTTON          CLICK    " + position );
                            listTask.getCheckedTasks().remove(position - (listTask.getUncheckedTasks().size()+1));
                            update();
                            //notifyItemRemoved(position);
                        }
                    });
                    holder.checkBoxListener.updatePosition(position);
                    onBind = true;
                    holder.checkBox.setChecked(true);
                    onBind = false;
                    System.out.println(position + " - " + listTask.getCheckedTasks().get(position - (listTask.getUncheckedTasks().size()+1)));
                    if(position>editTexts.size())
                        return;
                    if(editTexts.size() == position) {
                        editTexts.add(position, holder.editText);
                        Log.d("TAG", "Edit texts add = " + position );
                    }
                    else {
                        editTexts.remove(position);
                        editTexts.add(position, holder.editText);
                        Log.d("TAG", "Edit texts remove and add = " + position );
                    }

                    Log.d("TAG", "Edit texts size = " + editTexts.size() );
                }


                break;
            case 1:
                Log.d("TAG", "BUTTTON AND DELIVER" );
                if(listTask.getCheckedTasks().size()==0){
                    holder.deliver.setVisibility(GONE);
                    Log.d("TAG", "UNC = 0" );
                    Log.d("TAG", "VISIBLE GONE" );
                }
                else{
                    holder.deliver.setVisibility(VISIBLE);
                    Log.d("TAG", "UNC != 0" );
                    Log.d("TAG", "VISIBLE TRUE" );
                }
                break;
        }

    }

    public void requestFocusLast(){

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Log.d("TAG", "delay " );
                Log.d("TAG", "requestLastFocus" );
                Log.d("TAG", "focus to " + (editTexts.size()-1) );
                if(editTexts.size()>0)
                editTexts.get(editTexts.size()-1).requestFocus();
            }
        }, 1);
    }

    public void setFocusToEditText(){
        if(listTask.getUncheckedTasks().size()==0){
            listTask.getUncheckedTasks().add("");
            requestFocusTo(0);
            update();
        } else
            editTexts.get(0).requestFocus();
    }

    public void requestFocusTo(final int position){

        if(position<0)
            return;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Log.d("TAG", "delay " );
                Log.d("TAG", "requestLastTo " + position );
                if(editTexts.size()>0)
                    editTexts.get(position).requestFocus();
            }
        }, 1);
    }

    private void update(){
        Log.d("TAG", "update " );
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if(position == listTask.getUncheckedTasks().size())
            return 1;
        else
            return 0;
    }

    @Override
    public int getItemCount() {
        return listTask.getUncheckedTasks().size() + listTask.getCheckedTasks().size()+1;
    }

    private class EditTextListener implements TextWatcher, View.OnFocusChangeListener {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if(position<listTask.getUncheckedTasks().size())
                listTask.getUncheckedTasks().set(position, charSequence.toString());
            if(position > listTask.getUncheckedTasks().size())
                listTask.getCheckedTasks().set(position - (listTask.getUncheckedTasks().size() + 1), charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }


        @Override
        public void onFocusChange(View view, boolean b) {
            if(b)
                System.out.println("EDIT TEXT _  " + position + "Has focus");
        }
    }

    private class CheckBoxListener implements CompoundButton.OnCheckedChangeListener {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (!onBind) {
                if (b) {
                    listTask.getCheckedTasks().add(listTask.getUncheckedTask(position));
                    listTask.getUncheckedTasks().remove(position);
                    update();
                } else {
                    listTask.getUncheckedTasks().add(listTask.getCheckedTask(position - (listTask.getUncheckedTasks().size() + 1)));
                    listTask.getCheckedTasks().remove(position - (listTask.getUncheckedTasks().size()));
                    update();
                }
            }
        }
    }


}
