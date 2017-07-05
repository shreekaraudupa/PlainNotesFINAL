# PlainNotesFINAL
Note taking app

There are 5 java classes in this project namely MainActivity,DBOpenHelper,EditorActivity,NotesProvider,NotesCursorAdapterq . 
The DBOpenHelper class defines the database structure and creates the database in persistent storage.

	  class DBOpenHelper extends SQLiteOpenHelper
  

and in onCreate metohod create the table
	
	  sqLiteDatabase.execSQL(TABLE_CREATE);

But now we have to provide access to the database to rest of the application.
And for that ContentProvider is Used .
	
	  class NotesProvider extends ContentProvider

ContentProvider creates a standardized mechanism for getting to the application data.
Runnig all the task on UIthread may cause some problems so we have to move it to backend thread using a Loader.
Loader executes data operations on the background threads automatically.
Hence implement the Loader interface from MainActivity

	  class MainActivity extends AppCompatActivity
	  implements LoaderManager.LoaderCallbacks<Cursor>
	
To make the listView Better note_list_item.xml was created which has one ImageView and TextView .
Then inorder to store multiLine texts and long texts we are using the inbuilt CursorAdapter class.

	  class NotesCursorAdapter extends CursorAdapter
	
In mainActivity we can using simpleCursorAdapter and it knows how to pass texts directly from the cursor right into the layout.
But if we want to change the texts and the way its displayed dynamically we need to create our own custom cursor adapter.

	  class NotesCursorAdapter extends CursorAdapter
	
We have to implement two methods newView() and bindView() , newView() returns a view and it will be displayed on custom listView that we created .
bindView() returns an instance of the cursor object , and it will already point to the particular row of our database that is supposed to be displayed .

Then there is EditorActivity which recieves the intent from mainActivity and it has a menu and editText which will wdit text and even delete if wanted .

So overall you can delete create new note and also edit the existing notes and all the data is stored locally using sQlite databse .
