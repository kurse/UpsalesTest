# UpsalesTest

This app was made as part of a test for my application for Upsales.

# App Description
* The app fetches the first 100 clients and shows them in a list.  
* Every client on the list can be clicked to be shown on a new window with Map view on the client's address.  
* A SearchBar on the top allows to filter the Clients by name.  
* A Filter on the top right shows all the Account Managers and allows to filter the Clients by Account Manager.  
* This last part was made in the same spirit of the Upsales app.

# App Frameworks/Libraries/Design
* The app follow a Model-View-Presenter Design Pattern.
* The Upsales API communication was made using Retrofit2.  
* The asynchronous handling was made using RxAndroid.  
* Views were injected using ButterKnife.  
* Dependency Injection was made using Dagger2.  
* The Map view was made using the Google Maps for Android API.  
* The parsing for the models was made using the Gson library/annotations.  
* The app design was done using XML in Android Studio 
