import React from 'react';
import GoogleLogout from 'react-google-login';

const clientId = 'INSERT GOOGLE CLIENT ID HERE'

class Logout extends React.Component{

    onSuccess = () =>{
        console.log('Logout made sucessfully');
        this.props.loggingOut();
    };

    onFailure = (res) => {
		console.log('Logout failed');
	}

    render(){
        return (
            <div>
                <GoogleLogout
                    clientId={clientId}
                    buttonText="Logout"
                    onLogoutSuccess={this.onSuccess}
                    onFailure={this.onFailure}
                ></GoogleLogout>
            </div>
        )
    }
}

export default Logout;
