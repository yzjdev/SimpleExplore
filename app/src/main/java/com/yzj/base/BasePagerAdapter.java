package com.yzj.base;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;
import com.yzj.eplorer.fragment.FileListFragment;
import java.util.ArrayList;
import java.util.List;

public class BasePagerAdapter extends FragmentStatePagerAdapter {

	private FragmentManager mFragmentManager;
	private List<Fragment> mFragments=new ArrayList<>();
	
	public BasePagerAdapter(FragmentManager fm,List<Fragment> fragments){
		super(fm);
		this.mFragmentManager = fm;
		mFragments.clear();
		mFragments.addAll(fragments);
	}
	
	
	@Override
	public int getCount() {
		return mFragments.size();
	}

	@Override
	public Fragment getItem(int position) {
		return mFragments.get(position);
	}
	

	public void removeOther(int position) {
		Fragment f=mFragments.get(position);
		List<Fragment> datas=new ArrayList<>();
		datas.add(f);
		setData(datas);
	}


	public void removeData(int position) {
		mFragments.remove(position);
		List<Fragment> datas=new ArrayList<>();
		datas.addAll(mFragments);
		setData(datas);
	}


	public void addData(Fragment fragment) {
		mFragments.add(fragment);
		List<Fragment> datas=new ArrayList<>();
		datas.addAll(mFragments);
		setData(datas);
	}
	

	public void setData(List<Fragment> fragments){
		if(!mFragments.isEmpty()) mFragments.clear();
		mFragments.addAll(fragments);
		notifyDataSetChanged();
	}
	
	public void clear() {
		mFragments.clear();
		notifyDataSetChanged();
	}
	
	
	//将每一个item的id指定为fragment的hasCode，确保不会变更
	public long getItemId(int position) {
		return mFragments.get(position).hashCode();
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Fragment instantiateItemFragment = (Fragment) super.instantiateItem(container, position);
		Fragment itemFragment = mFragments.get(position);
		//如果集合中对应下标的fragment和fragmentManager中的对应下标的fragment对象一致，则直接返回该fragment
		if (instantiateItemFragment == itemFragment) {
			return instantiateItemFragment;
		} else {
			//如果集合中对应下标的fragment和fragmentManager中的对应下标的fragment对象不一致，那么就是新添加的，所以自己add进入；
			mFragmentManager.beginTransaction().add(container.getId(), itemFragment).commit();
			return itemFragment;
		}
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		Fragment fragment = (Fragment) object;
		//如果getItemPosition中的值为PagerAdapter.POSITION_NONE，就执行该方法。
		if (mFragments.contains(fragment)) {
			super.destroyItem(container, position, object);
			return;
		}
		//自己执行移除。因为mFragments在删除的时候就把某个fragment对象移除了，所以一般都得自己移除在fragmentManager中的该对象。
		if (!mFragmentManager.isStateSaved()) {
			mFragmentManager.beginTransaction().remove(fragment).commit();
		}
	}

	@Override
	public int getItemPosition(Object object) {
		//如果fragment还没有添加过，或者没有包含在里面，则返回没有找到
		if (!((Fragment) object).isAdded() || !mFragments.contains(object)) {
			return POSITION_NONE;
		}
		//否则就返回列表中的位置
		return mFragments.indexOf(object);
	}
	
	
}
