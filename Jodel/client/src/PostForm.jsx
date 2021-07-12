import React from "react";
import { withStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import axios from 'axios';
import Alert from '@material-ui/lab/Alert';

const styles = theme => ({
	center: {
		display: 'block',
		borderTop: '5px solid black',
		padding: '10px',
		marginTop: '10px',
	},
	content: {
		display: "flex",
		justifyContent: "center",
  		alignItems: "center",
	},
	textfield: {
		width: "50%",
	},
	button: {
		backgroundColor: "red",
		margin: 10
	}
});

class PostForm extends React.Component {

	constructor(props) {
		super(props);
	    this.state = {	  
            data: [],
			message: false,
			text: '',
		};		
	}

	//method for input text field
	handleChange = name => event => {
		this.setState({
		  text: event.target.value,
		});
	};

	componentDidMount(){
		console.log("Current User: ", this.props.currentUser);
	}

	createComment(){
		if ("geolocation" in navigator && this.state.text!='') {
			console.log("Available");
			console.log("Logged in User: " , this.props.currentUser);
			console.log("Post: ", this.props.post.post_id);
			navigator.geolocation.getCurrentPosition((position) => {

				var date;
				date = new Date();
				date = date.getUTCFullYear() + '-' +
					('00' + (date.getUTCMonth()+1)).slice(-2) + '-' +
					('00' + date.getUTCDate()).slice(-2)
				console.log(date);
	
				console.log("Latitude is :", position.coords.latitude);
				console.log("Longitude is :", position.coords.longitude);
				console.log("TEXT: ", this.state.text);
				console.log("Date: " , date);
	
				axios.post(this.props.url + "comments/createComment?" + 
				"lat=" + position.coords.latitude + 
				"&lon=" + position.coords.longitude + 
				"&date=" + date + 
				"&text=" + this.state.text + 
				"&postid=" + this.props.post.post_id + 
				"&userid=" +  this.props.currentUser.user_id)
				.then((response) => {
					this.createVoting(response.data.comment_id);
					//show successmessage for 3 seconds
					this.setState({message: true})
					setTimeout(() => {
						this.setState({message: ""})
					}, 3000);
					this.props.parentCallBack("Data from child");
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

	createVoting(comment_id){
		console.log("Versuche voting zu erstellen");
		axios.post(this.props.url + "votings/createVoting?commentid=" + comment_id)
		.then((response)=>{
			console.log("Voting erstellen hat geklappt");
		})
		.catch((error)=>{
			console.log("error while axios");
		});
	}
  
	render() {
		const { classes } = this.props;
		let successMessage;
		if(this.state.message){
			successMessage = (
				<Alert variant="filled" severity="success">
				</Alert>
			);
		}
		return (
		<div className={classes.center}>
			<div className={classes.content}>
				<div className={classes.textfield}>
					<TextField
						id="outlined-multiline-flexible"
						label="Type text here"
						multiline
						rowsMax="4"
						value={this.state.text}
						onChange={this.handleChange('multiline')}
						className={classes.textField}
						variant="outlined"
						fullWidth
					/>
				</div>
				<div className={classes.button}>
					<Button variant="contained" color="secondary" onClick={() => this.createComment()}>
						add comment
					</Button>
				</div>
				<div>
					{successMessage}
				</div>
			</div>
	    </div>
		);
	}
}

export default withStyles(styles)(PostForm);

