import React from "react";
import { withStyles } from '@material-ui/core/styles';
import ExpandLessIcon from '@material-ui/icons/ExpandLess';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import IconButton from '@material-ui/core/IconButton';
import Typography from '@material-ui/core/Typography';
import axios from "axios";

const styles = theme => ({
	center: {
		// display: 'flex', 
		justifyContent: 'center',
		alignItems: 'center',
		// minHeight: '100vh',
		// border: "1px solid red",
		// width: "10%",
	}
});

class CommentForm extends React.Component {

	constructor(props) {
		super(props);
	    this.state = {	  
            data: [],
			voted: false,
		};		
	}

	decreaseVoting(){
		console.log("Voting verringern");
		if(this.props.value>0){
			axios.post(this.props.url + "votings/updateVoting?votingid=" + this.props.voting.voting_id + 
			"&value=" + (this.props.value-1) + 
			"&commentid=" + this.props.comment.comment_id +
			"&userid=" + this.props.currentUser.user_id)
		.then((response) => {
			console.log("Updated Voting" , response.data);
			this.props.parentCallBack("Data from child CommentForm");
			setTimeout(() => {
				this.props.parentCallBack('');
			}, 100);
		})
		.catch((error) => {
			console.log(error);
		});
		}
	}

	increaseVoting(){
		console.log("Voting erhöhen", this.props.voting);
		axios.post(this.props.url + "votings/updateVoting?votingid=" + this.props.voting.voting_id + 
			"&value=" + (this.props.value+1) + 
			"&commentid=" + this.props.comment.comment_id + 
			"&userid=" + this.props.currentUser.user_id)
		.then((response) => {
			console.log("Updated Voting" , response.data);
			this.props.parentCallBack("Data from child CommentForm");
			setTimeout(() => {
				this.props.parentCallBack('');
			}, 1);
		})
		.catch((error) => {
			console.log(error);
		});
	}

	componentDidMount(){
		setTimeout(() => {
			axios.get(this.props.url + "votings/getVoted?votingid=" + this.props.voting.voting_id + "&userid=" + this.props.currentUser.user_id)
			.then((response) => {
				console.log("Hat schon gevoted:" , response.data);
				this.setState({voted: response.data});
			})
			.catch((error) => {
				console.log(error);
			});
		}, 100);
		
	}

	
	render() {
		const { classes } = this.props;
		console.log("voting value: ", this.props.value);
		let votedUp;
		let votedDown;
		if(this.state.voted){
			votedUp = (
				<IconButton color="secondary" aria-label="upload picture" component="span" onClick={() => this.increaseVoting()}>
					<ExpandLessIcon/>
				</IconButton>
			);
			votedDown = (
				<IconButton color="secondary" aria-label="upload picture" component="span" onClick={() => this.decreaseVoting()}>
					<ExpandMoreIcon/>
				</IconButton>
			)
		}else{
			votedUp = (
				<IconButton color="primary" aria-label="upload picture" component="span" onClick={() => this.increaseVoting()}>
					<ExpandLessIcon/>
				</IconButton>
			)
			votedDown = (
				<IconButton color="primary" aria-label="upload picture" component="span" onClick={() => this.decreaseVoting()}>
					<ExpandMoreIcon/>
				</IconButton>
			)
		}
		return (
		<div className={classes.center}>
			{/* <h1>This component renders the input form for a comment</h1> */}
			<div>
				{votedUp}
			</div>
			<div>
				<Typography align="center" variant="h6">
					{this.props.value}
				</Typography>
			</div>
			<div>
				{votedDown}
			</div>
	    </div>
		);
	}
}

export default withStyles(styles)(CommentForm);

