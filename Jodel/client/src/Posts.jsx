import React from "react";
import { withStyles } from '@material-ui/core/styles';
import PostList from './PostList';
import axios from 'axios';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import Alert from '@material-ui/lab/Alert';
import Logout from './Logout';
import Snackbar from '@material-ui/core/Snackbar';


const styles = theme => ({
	root:{
		// backgroundColor: "black",
		top: 0,
		// backgroundColor: '#282929'
		backgroundColor: 'white',
	},
	center: {
		display: 'block',
		justifyContent: 'center',
		alignItems: 'center',
		minHeight: 'calc(100vh - 40px)',
		// border: '5px solid red',
		// padding: '10px',
	},
	// margin: {
	// 	"&:hover" :{
	// 		backgroundColor: "#193cbd",
	// 	},
	// 	margin: theme.spacing(1),
	// 	backgroundColor: "white",
	// },
	buttonBar: {
		backgroundColor: "#5cb3b8",
		display: "flex",
		justifyContent: "center",
  		alignItems: "center",
	},
	button: {
		backgroundColor: "red",
		margin: 10
	}
});




class Posts extends React.Component {

	constructor(props) {
		super(props);
	    this.state = {	  
            data: [],
			currentUser: [],
			text: "",
			update: '',
			open: false,
			vertical: 'top',
			horizontal: 'center',
		};		
	}

	//method for input text field
	handleChange = name => event => {
		this.setState({
		  text: event.target.value,
		});
	};

	componentDidMount(){
		this.setState({currentUser: this.props.currentUser});
	}

	handleClose = () => {
		this.setState({ open: false });
	  };

	createPost(){
		if ("geolocation" in navigator && this.state.text!="") {
			console.log("Available");
			console.log("Logged in User: " , this.props.currentUser);
			navigator.geolocation.getCurrentPosition((position) => {
				//getting date in utc time format, because it's the coordinated global time 
				var date;
				date = new Date();
				date = date.getUTCFullYear() + '-' +
					('00' + (date.getUTCMonth()+1)).slice(-2) + '-' +
					('00' + (date.getUTCDate())).slice(-2)
				console.log("Latitude is :", position.coords.latitude);
				console.log("Longitude is :", position.coords.longitude);
				console.log("TEXT: ", this.state.text);
				console.log("Date: " , date);
	
				axios.post(this.props.url + "posts/createPost?" + 
				"lat=" + position.coords.latitude + 
				"&lon=" + position.coords.longitude + 
				"&text=" + this.state.text + 
				"&date=" + date + 
				"&userid=" +  this.props.currentUser.user_id)
				.then((response) => {
					this.setState({open: true});
					console.log(response.data);
					//changing our state and change it back to trigger reload of our PostList Component
					this.setState({update: 'changed'})
					setTimeout(()=> {
						this.setState({update: ''})
					}, 1);

					//clear textfield
					this.setState({text: ''});
				})
				.catch((error) => {
					console.log("error while axios");
				});	
			});
		} else {
			console.log("Not Available");
		}
	}
  
	render() {
		const { classes } = this.props;
		return (
			<div className={classes.root}>
				<div className={classes.center}>
				{/* <h1>Here goes the posts list plus buttons for adding and editing posts</h1> */}
					<div className={classes.buttonBar}>
						<div>
							<TextField
								id="outlined-multiline-flexible"
								label="Type text here"
								multiline
								rowsMax="4"
								value={this.state.text}
								onChange={this.handleChange('multiline')}
								className={classes.textField}
								margin="normal"
								variant="outlined"
							/>
						</div>
						<div className={classes.button}>
							<Button variant="contained" color="secondary" onClick={() => this.createPost({vertical: 'top', horizontal: 'center'})}>
								create Post
							</Button>
						</div>
					</div>
					<PostList currentUser={this.state.currentUser} update={this.state.update} url={this.props.url}></PostList>
	    		</div>
				<Snackbar
					autoHideDuration={4000}
					open={this.state.open}
					onClose={this.handleClose}
				>
					<Alert onClose={this.handleClose} severity="success">
          				Post sucessfully created
        			</Alert>
				</Snackbar>
			</div>
		);
	}
}

export default withStyles(styles)(Posts);

