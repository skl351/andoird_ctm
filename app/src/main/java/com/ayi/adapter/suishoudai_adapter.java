package com.ayi.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ayi.R;
import com.ayi.entity.item_suishoudai;
import com.ayi.home_page.Suishoudai_thing;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/30.
 */
public class suishoudai_adapter extends BaseAdapter {

    List<TextView> list_text = new ArrayList<>();
    private List<item_suishoudai> list;
    private Context context;

    public suishoudai_adapter(List<item_suishoudai> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (list_text == null) {
            list_text = new ArrayList<>();
        }

        convertView = LayoutInflater.from(context).inflate(R.layout.suishoudai_item_item, null);
        ImageView icon1 = (ImageView) convertView.findViewById(R.id.icon1);
        TextView text1 = (TextView) convertView.findViewById(R.id.text1);
        ImageLoader.getInstance().displayImage(list.get(position).getSmall_img(), icon1);
        text1.setText(list.get(position).getName());
        final TextView num1 = (TextView) convertView.findViewById(R.id.num1);
        list_text.add(position, num1);
        System.out.println("-1--" + position + "=1==" + num1);
        View jiahao = convertView.findViewById(R.id.jiahao);
        View jianhao = convertView.findViewById(R.id.jianhao);
        final ImageView image_jiahao = (ImageView) convertView.findViewById(R.id.image_jiahao);
        final ImageView image_jianhao = (ImageView) convertView.findViewById(R.id.image_jianhao);
        num1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView num1 = (TextView) v;
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                final AlertDialog alertDialog = dialogBuilder.create();

                View layout = LayoutInflater.from(context).inflate(R.layout.shurusuishoudaitext, null);
                final EditText e1 = (EditText) layout.findViewById(R.id.num2);
                e1.setText(num1.getText().toString());
                System.out.println("-2--" + position + "=2==" + num1);
                e1.setSelection(e1.getText().toString().length());
                e1.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String c = "";
                        if (s.toString().equals("")) {
                            c = "0";
                        } else {
                            c = s.toString();
                        }
                        if (s.toString().length() > 1) {
                            String ss = s.toString().substring(0, 1);
                            if (ss.equals("0")) {
                                c = s.toString().substring(1, 2);
                            }
                        }
                        if (!c.equals(s.toString())) {
                            e1.setText(c);
                            e1.setSelection(e1.getText().toString().length());
                        }
                    }
                });
                View left = layout.findViewById(R.id.cancel_btn);
                View right = layout.findViewById(R.id.ok_btn);

                left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        num1.setText(e1.getText().toString());
                        System.out.println("zheli" + e1.getText().toString());
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setView(layout);
                alertDialog.show();
            }
        });
        num1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("0")) {
                    image_jianhao.setBackgroundResource(R.mipmap.jianhao);
                    image_jiahao.setBackgroundResource(R.mipmap.jiahao_lvse);
                } else if (s.toString().equals("99")) {
                    image_jianhao.setBackgroundResource(R.mipmap.jianhao_lvse);
                    image_jiahao.setBackgroundResource(R.mipmap.jiahao);
                } else {
                    image_jianhao.setBackgroundResource(R.mipmap.jianhao_lvse);
                    image_jiahao.setBackgroundResource(R.mipmap.jiahao_lvse);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                System.out.println("测试" + s.toString());
                String c = "";
                if (s.toString().equals("")) {
                    c = "0";
                } else {
                    c = s.toString();
                }

                list.get(position).setNum(c);
            }
        });
        jiahao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(list.get(position).getNum()) + 1 <= 99) {
                    num1.setText("" + (Integer.parseInt(list.get(position).getNum()) + 1));

                }
            }
        });
        jianhao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(list.get(position).getNum()) - 1 >= 0) {
                    num1.setText("" + (Integer.parseInt(list.get(position).getNum()) - 1));
                }

            }
        });


        if (list.get(position).getNum() != null) {
            num1.setText(list.get(position).getNum());
        } else {
//            System.out.println("这2里"+list.get(position).getNum()+"---");
            num1.setText("0");
        }
        icon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Suishoudai_thing.class);
                intent.putExtra("flag", list.get(position).getId());
                intent.putExtra("num", "" + position);
                intent.putExtra("biaohao", list.get(position).getNum());

                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
