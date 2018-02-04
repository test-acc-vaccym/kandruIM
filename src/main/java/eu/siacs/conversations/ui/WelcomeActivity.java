package eu.siacs.conversations.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;

import java.util.List;

import eu.siacs.conversations.R;
import eu.siacs.conversations.entities.Account;
import eu.siacs.conversations.utils.XmppUri;

public class WelcomeActivity extends XmppActivity {

	public static final String EXTRA_INVITEE = "eu.siacs.conversations.invitee";

	@Override
	protected void refreshUiReal() {

	}

	@Override
	void onBackendConnected() {

	}

	@Override
	public void onStart() {
		super.onStart();
		final int theme = findTheme();
		if (this.mTheme != theme) {
			recreate();
		}
	}

	@Override
	public void onNewIntent(Intent intent) {
		if (intent != null) {
			setIntent(intent);
		}
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		if (getResources().getBoolean(R.bool.portrait_only)) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		final ActionBar ab = getActionBar();
		if (ab != null) {
			ab.setDisplayShowHomeEnabled(false);
			ab.setDisplayHomeAsUpEnabled(false);
		}
		final Button createAccount = (Button) findViewById(R.id.create_account);
		createAccount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//KANDRUIM REGISTRATION
				Intent browseIntent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://account.kandru.de"));
				startActivity(browseIntent);
			}
		});
		final Button useOwnProvider = findViewById(R.id.use_own_provider);
		useOwnProvider.setOnClickListener(v -> {
			List<Account> accounts = xmppConnectionService.getAccounts();
			Intent intent = new Intent(WelcomeActivity.this, EditAccountActivity.class);
			if (accounts.size() == 1) {
				intent.putExtra("jid", accounts.get(0).getJid().toBareJid().toString());
				intent.putExtra("init", true);
			} else if (accounts.size() >= 1) {
				intent = new Intent(WelcomeActivity.this, ManageAccountActivity.class);
			}
			addInvitee(intent);
			startActivity(intent);
		});

	}

	public void addInvitee(Intent intent) {
		addInvitee(intent, getIntent());
	}

	public static void addInvitee(Intent intent, XmppUri uri) {
		if (uri.isJidValid()) {
			intent.putExtra(EXTRA_INVITEE, uri.getJid().toString());
		}
	}

	public static void addInvitee(Intent to, Intent from) {
		if (from != null && from.hasExtra(EXTRA_INVITEE)) {
			to.putExtra(EXTRA_INVITEE, from.getStringExtra(EXTRA_INVITEE));
		}
	}

}
