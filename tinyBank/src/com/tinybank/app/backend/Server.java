package com.tinybank.app.backend;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.raweng.built.Built;
import com.raweng.built.BuiltError;
import com.raweng.built.BuiltObject;
import com.raweng.built.BuiltQuery;
import com.raweng.built.BuiltResultCallBack;
import com.raweng.built.BuiltUser;
import com.raweng.built.QueryResult;
import com.raweng.built.QueryResultsCallBack;
import com.tinybank.app.bean.Feed;
import com.tinybank.app.bean.TinyAccount;
import com.tinybank.app.bean.User;
import com.tinybank.app.event.BankAccountEvent;
import com.tinybank.app.event.EventBus;
import com.tinybank.app.event.LoginEvent;
import com.tinybank.app.event.TinyAccountsEvent;
import com.tinybank.app.event.UserFeedsEvent;

public class Server {

	private static Context context;

	public static void connect(Context context) {

		if (context != null) {
			Server.context = context;
			try {
				Built.initializeWithApiKey(context, "blt5d6ff099d763bf99", "tinybank");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void login(String email, String password) {
		
		final BuiltUser builtUserObject = new BuiltUser();
		builtUserObject.login(email, password,
				new BuiltResultCallBack() {
					@Override
					public void onSuccess() {
						// user has logged in successfully
						// builtUserObject.authtoken contains the session authtoken
						String username = builtUserObject.getUserName();
						String email = builtUserObject.getEmailId();
						String first_name = builtUserObject.getFirstName();
						String last_name = builtUserObject.getLastName();
						String type = (String)builtUserObject.get("user_type");
						Log.e("tinybank", builtUserObject.toJSON().toString());
						//TODO REMOVE
						if (type == null) {
							if (username.equals("shira")) {
								type = "parent";
							} else {
								type = "tiny";
							}
						}
							
						boolean isParent = (type.equals("parent") ? true : false);
						User user = new User(username, isParent, email, first_name, last_name);
						EventBus.postOnMain(context, new LoginEvent(user, true));
						
					}

					@Override
					public void onError(BuiltError builtErrorObject) {
						Log.i("error: ", "" + builtErrorObject.getErrorMessage());
						Log.i("error: ", "" + builtErrorObject.getErrorCode());
						Log.i("error: ", "" + builtErrorObject.getErrors());
						EventBus.postOnMain(context, new LoginEvent(null, false));
					}

					@Override
					public void onAlways() {
					}
				});
	}
	
	public static void getBankAccount(String username) {

	    BuiltQuery query = new BuiltQuery("bank_account");
	    query.where("username", username);
	    
	    query.exec(new QueryResultsCallBack() {
	    	
		    @Override
		    public void onSuccess(QueryResult queryResultObject) {
			    List<BuiltObject> objects = queryResultObject.getResultObjects();
			    
			    for (Object object : objects) {
			    	
			    	BuiltObject bankAccount = (BuiltObject) object;
			    	String bank_account_id = (String)bankAccount.get("bank_account_id");
			    	String bank_type = (String)bankAccount.get("bank_type");
			    	Double balance = null;
					try {
						balance = (Double)bankAccount.get("bank_account_balance");
					} catch (ClassCastException e) {
						balance = Double.valueOf((Integer)bankAccount.get("bank_account_balance"));
					}
			    	String bank_name = (String)bankAccount.get("bank_name");
			    	EventBus.postOnMain(context, new BankAccountEvent(true, bank_account_id, bank_type, bank_name, balance));
				}
		    }
		    
		    @Override
		    public void onError(BuiltError builtErrorObject) {
			    // query failed
			    // the message, code and details of the error
			    Log.i("error: ", "" + builtErrorObject.getErrorMessage());
			    Log.i("error: ", "" + builtErrorObject.getErrorCode());
			    Log.i("error: ", "" + builtErrorObject.getErrors());
			    EventBus.postOnMain(context, new BankAccountEvent(false, null, null, null, null));
		    }
		    
		    @Override
		    public void onAlways() {
		    }
	    });
	}
	public static void getTinyAccounts(String bank_account_id) {
		
		BuiltQuery query = new BuiltQuery("tiny_account");
		query.where("bank_account_id", bank_account_id);
		
		query.exec(new QueryResultsCallBack() {
			
			@Override
			public void onSuccess(QueryResult queryResultObject) {
				List<BuiltObject> objects = queryResultObject.getResultObjects();
				ArrayList<TinyAccount> tinyAccounts = new ArrayList<TinyAccount>();
				
				for (Object object : objects) {
					
					BuiltObject tinyAccount = (BuiltObject) object;
					String username = (String)tinyAccount.get("username");
					Double balance = null;
					try {
						balance = (Double)tinyAccount.get("bank_account_balance");
					} catch (ClassCastException e) {
						balance = Double.valueOf((Integer)tinyAccount.get("bank_account_balance"));
					}
					tinyAccounts.add(new TinyAccount(username, balance));
				}
				EventBus.postOnMain(context, new TinyAccountsEvent(true, tinyAccounts));
			}
			
			@Override
			public void onError(BuiltError builtErrorObject) {
				// query failed
				// the message, code and details of the error
				Log.i("error: ", "" + builtErrorObject.getErrorMessage());
				Log.i("error: ", "" + builtErrorObject.getErrorCode());
				Log.i("error: ", "" + builtErrorObject.getErrors());
				EventBus.postOnMain(context, new TinyAccountsEvent(false, null));
			}
			
			@Override
			public void onAlways() {
			}
		});
	}
	public static void depositMoney(final String username, String description, final Double amount, final boolean isParent) {

	    BuiltObject object = new BuiltObject("feed");
	    object.set("username", username);
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
	    object.set("action_date", df.format(new Date()));
	    object.set("action_type", "deposit");
	    object.set("action_description", description);
	    object.set("action_amount", Double.toString(amount));
	    if (isParent) {
	    	object.set("action_status", "approved");
	    } else {
	    	object.set("action_status", "pending");
	    }
	    
	    object.save(new BuiltResultCallBack() {
		    @Override
		    public void onSuccess() {
		    	if (isParent) {
		    		addTinyAmount(username, amount);
		    	}
		    }
		    @Override
		    public void onError(BuiltError builtErrorObject) {
		    	Log.i("error: ", "" + builtErrorObject.getErrorMessage());
				Log.i("error: ", "" + builtErrorObject.getErrorCode());
				Log.i("error: ", "" + builtErrorObject.getErrors());
		    }
		    @Override
		    public void onAlways() {
		    // write code here that you want to execute
		    // regardless of success or failure of the operation
		    }
	    });
	}
	private static void addTinyAmount(String username, final Double amount) {
		
		BuiltQuery query = new BuiltQuery("tiny_account");
		query.where("username", username);
		
		query.exec(new QueryResultsCallBack() {
			
			@Override
			public void onSuccess(QueryResult queryResultObject) {
				BuiltObject tinyAccount = queryResultObject.getResultObjects().get(0);
				Double balance = null;
				try {
					balance = (Double)tinyAccount.get("bank_account_balance");
				} catch (ClassCastException e) {
					balance = Double.valueOf((Integer)tinyAccount.get("bank_account_balance"));
				}
				balance += amount;
				
				// setUid will identify the object, and calling save will update it
				BuiltObject object = new BuiltObject("tiny_account");
				object.setUid(tinyAccount.getUid());
				object.set("bank_account_balance", Double.toString(balance));
				object.save(new BuiltResultCallBack() {
					@Override
					public void onSuccess() {
						Log.e("tinybank", "success");
					}
					@Override
					public void onError(BuiltError builtErrorObject) {
						Log.e("error: ", "" + builtErrorObject.getErrorMessage());
						Log.e("error: ", "" + builtErrorObject.getErrorCode());
						Log.e("error: ", "" + builtErrorObject.getErrors());
						// there was an error in updating the object
						// builtErrorObject will contain more details
					}
					@Override
					public void onAlways() {
						// write code here that you want to execute
						// regardless of success or failure of the operation
					}
				});
				
				tinyAccount.set("bank_account_balance", Double.toString(balance));
				
				tinyAccount.save(new BuiltResultCallBack() {
					@Override
					public void onSuccess() {
					// object is updated successfully
					}
					@Override
					public void onError(BuiltError builtErrorObject) {
					// there was an error in updating the object
					// builtErrorObject will contain more details
					}
					@Override
					public void onAlways() {
					// write code here that you want to execute
					// regardless of success or failure of the operation
					}
				});
			}
			
			@Override
			public void onError(BuiltError builtErrorObject) {
				// query failed
				// the message, code and details of the error
				Log.i("error: ", "" + builtErrorObject.getErrorMessage());
				Log.i("error: ", "" + builtErrorObject.getErrorCode());
				Log.i("error: ", "" + builtErrorObject.getErrors());
				EventBus.postOnMain(context, new TinyAccountsEvent(false, null));
			}
			
			@Override
			public void onAlways() {
			}
		});
	}
	public static void approveDeposit(String depositUid, final String username, final Double amount) {
		BuiltObject object = new BuiltObject("feed");
		object.setUid(depositUid);
		object.set("action_status", "approved");
		object.save(new BuiltResultCallBack() {
			@Override
			public void onSuccess() {
				addTinyAmount(username, amount);
			}
			@Override
			public void onError(BuiltError builtErrorObject) {
				Log.e("error: ", "" + builtErrorObject.getErrorMessage());
				Log.e("error: ", "" + builtErrorObject.getErrorCode());
				Log.e("error: ", "" + builtErrorObject.getErrors());
				// there was an error in updating the object
				// builtErrorObject will contain more details
			}
			@Override
			public void onAlways() {
				// write code here that you want to execute
				// regardless of success or failure of the operation
			}
		});
	}
	public static void rejectDeposit(String depositUid) {
		BuiltObject object = new BuiltObject("feed");
		object.setUid(depositUid);
		object.set("action_status", "rejected");
		object.save(new BuiltResultCallBack() {
			@Override
			public void onSuccess() {
			}
			@Override
			public void onError(BuiltError builtErrorObject) {
				Log.e("error: ", "" + builtErrorObject.getErrorMessage());
				Log.e("error: ", "" + builtErrorObject.getErrorCode());
				Log.e("error: ", "" + builtErrorObject.getErrors());
			}
			@Override
			public void onAlways() {
				// write code here that you want to execute
				// regardless of success or failure of the operation
			}
		});
	}
	public static void getUserFeed(final String username) {
		BuiltQuery query = new BuiltQuery("feed");
		query.where("username", username);
		query.descending("action_date");
		
		query.exec(new QueryResultsCallBack() {

			@Override
			public void onSuccess(QueryResult queryResultObject) {
				List<BuiltObject> objects = queryResultObject.getResultObjects();
				ArrayList<Feed> userFeeds = new ArrayList<Feed>();
				
				for (Object object : objects) {
					
					BuiltObject feed = (BuiltObject) object;
					String uid = feed.getUid();
					String username = (String)feed.get("username");
					String date = (String)feed.get("action_date");
					String type = (String)feed.get("action_type");
					String description = (String)feed.get("action_description");
					String status = (String)feed.get("action_status");
					Double amount = null;
					try {
						amount = (Double)feed.get("action_amount");
					} catch (ClassCastException e) {
						amount = Double.valueOf((Integer)feed.get("action_amount"));
					}
					userFeeds.add(new Feed(uid, username, date, type, description, amount, status));
				}
				EventBus.postOnMain(context, new UserFeedsEvent(true, userFeeds));
			}
			
			@Override
			public void onAlways() {
			}

			@Override
			public void onError(BuiltError builtErrorObject) {
				Log.e("error: ", "" + builtErrorObject.getErrorMessage());
				Log.e("error: ", "" + builtErrorObject.getErrorCode());
				Log.e("error: ", "" + builtErrorObject.getErrors());
			}

			
		});
	}
}
