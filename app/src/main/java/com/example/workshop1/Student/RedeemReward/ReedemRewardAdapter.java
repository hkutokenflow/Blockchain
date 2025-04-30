package com.example.workshop1.Student.RedeemReward;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workshop1.R;

import java.util.List;

// RewardAdapter.java
public class ReedemRewardAdapter extends RecyclerView.Adapter<ReedemRewardAdapter.ViewHolder> {

    private List<RewardItem> rewardList;
    private Context context;

    public ReedemRewardAdapter(Context context, List<RewardItem> rewards) {
        this.context = context;
        this.rewardList = rewards;
    }


    @NonNull
    @Override
    public ReedemRewardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reward, parent, false);
        return new ReedemRewardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RewardItem reward = rewardList.get(position);
        holder.title.setText(reward.title);
        holder.tokens.setText(reward.tokens + " Tokens");

        // ----------------截取Description的前40个字-----------------------------------
        String fullDesc = reward.description != null ? reward.description : "";
        String preview = fullDesc.length() > 40 ? fullDesc.substring(0, 40) + "..." : fullDesc;
        holder.shortDesc.setText(preview);

        holder.itemView.setOnClickListener(v -> showRewardDialog(context, reward));
    }



    private void showRewardDialog(Context context, RewardItem reward) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_reward_description);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView title = dialog.findViewById(R.id.dialog_title);
        TextView desc = dialog.findViewById(R.id.dialog_description);
        TextView tokens = dialog.findViewById(R.id.dialog_tokens);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        Button btnConfirm = dialog.findViewById(R.id.btn_confirm);

        title.setText(reward.title);
        desc.setText(reward.description);
        tokens.setText(reward.tokens + " Tokens");

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnConfirm.setOnClickListener(v -> {
            Toast.makeText(context, "Reward Redeemed!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }


    @Override
    public int getItemCount() {
        return rewardList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, tokens, shortDesc;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.reward_title);
            tokens = itemView.findViewById(R.id.reward_tokens);
            shortDesc = itemView.findViewById(R.id.reward_short_desc);
        }
    }
}
