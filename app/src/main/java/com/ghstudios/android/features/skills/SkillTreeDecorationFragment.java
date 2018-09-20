package com.ghstudios.android.features.skills;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ghstudios.android.AssetLoader;
import com.ghstudios.android.data.classes.ItemToSkillTree;
import com.ghstudios.android.data.cursors.ItemToSkillTreeCursor;
import com.ghstudios.android.loader.ItemToSkillTreeListCursorLoader;
import com.ghstudios.android.mhgendatabase.R;
import com.ghstudios.android.ClickListeners.DecorationClickListener;
import com.ghstudios.android.features.decorations.detail.DecorationDetailActivity;

public class SkillTreeDecorationFragment extends ListFragment implements
		LoaderCallbacks<Cursor> {

	private static final String ARG_SKILL = "SKILLTREE_SKILL";
	private static final String ARG_TYPE = "SKILLTREE_TYPE";

	public static SkillTreeDecorationFragment newInstance(long skill, String type) {
		Bundle args = new Bundle();
		args.putLong(ARG_SKILL, skill);
		args.putString(ARG_TYPE, type);
		SkillTreeDecorationFragment f = new SkillTreeDecorationFragment();
		f.setArguments(args);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Initialize the loader to load the list of runs
		getLoaderManager().initLoader(R.id.skill_tree_decoration_fragment, getArguments(), this);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_generic_list, container, false);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// The id argument will be the Item ID; CursorAdapter gives us this for free

		Intent i = new Intent(getActivity(), DecorationDetailActivity.class);
		i.putExtra(DecorationDetailActivity.EXTRA_DECORATION_ID, (long) v.getTag());
		startActivity(i);
	}


	@Override
	public @NonNull Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
		// You only ever load the runs, so assume this is the case
		long mSkill = -1;
		String mType = null;
		if (args != null) {
			mSkill = args.getLong(ARG_SKILL);
			mType = args.getString(ARG_TYPE);
		}
		return new ItemToSkillTreeListCursorLoader(getActivity(), 
				ItemToSkillTreeListCursorLoader.FROM_SKILL_TREE, mSkill, mType);
	}

	@Override
	public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
		// Create an adapter to point at this cursor
		ItemToSkillTreeListCursorAdapter adapter = new ItemToSkillTreeListCursorAdapter(
				getActivity(), (ItemToSkillTreeCursor) cursor);
		setListAdapter(adapter);

	}

	@Override
	public void onLoaderReset(@NonNull Loader<Cursor> loader) {
		// Stop using the cursor (via the adapter)
		setListAdapter(null);
	}

	private static class ItemToSkillTreeListCursorAdapter extends CursorAdapter {

		private ItemToSkillTreeCursor mItemToSkillTreeCursor;

		public ItemToSkillTreeListCursorAdapter(Context context, ItemToSkillTreeCursor cursor) {
			super(context, cursor, 0);
			mItemToSkillTreeCursor = cursor;
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			// Use a layout inflater to get a row view
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			return inflater.inflate(R.layout.fragment_skill_item_listitem,
					parent, false);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			// Get the skill for the current row
			ItemToSkillTree skill = mItemToSkillTreeCursor.getItemToSkillTree();

			// Set up the text view
			LinearLayout root = (LinearLayout) view.findViewById(R.id.listitem);
			ImageView skillItemImageView = (ImageView) view.findViewById(R.id.item_image);
			TextView skillItemTextView = (TextView) view.findViewById(R.id.item);
			TextView skillAmtTextView = (TextView) view.findViewById(R.id.amt);
			
			String nameText = skill.getItem().getName();
			String amtText = "" + skill.getPoints();
			
			skillItemTextView.setText(nameText);
			skillAmtTextView.setText(amtText);

			AssetLoader.setIcon(skillItemImageView,skill.getItem());
			
			root.setTag(skill.getItem().getId());
            root.setOnClickListener(new DecorationClickListener(context, skill.getItem().getId()));
		}
	}

}
