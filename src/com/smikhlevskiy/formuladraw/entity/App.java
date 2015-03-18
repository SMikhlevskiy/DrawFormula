/** <h1>App</h1>
 *  The App is class for work on start program
 *  Initialize Parse.com
 *  @author  SMikhlevskiy
 *  @version 1.0
 *  @since   2015-03
 */
package com.smikhlevskiy.formuladraw.entity;

import com.parse.Parse;
import com.smikhlevskiy.formuladraw.R;

import android.app.Application;
import android.widget.Toast;

public class App extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		
		Parse.enableLocalDatastore(this);
	    Parse.initialize(this, "49TMtDqnUx9cWNssiQfrK8SZS1dkQIR2ai3iH4vZ", "eQ9MvhA8hMcORp4dceFRJkNtHXGK7PYCDq7DjflY");

	}
}
