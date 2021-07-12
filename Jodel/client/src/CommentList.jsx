import React from "react";
import { withStyles } from '@material-ui/core/styles';
import Comment from './Comment';
import axios from "axios";

const styles = theme => ({
	center: {
		display: 'block', 
		// justifyContent: 'center',
		alignItems: 'center',
		// minHeight: '100vh',
	}
});

class CommentList extends React.Component {

	constructor(props) {
		super(props);
	    this.state = {	  
            comments: [],
            ownPosts: false,
            noPosition: false,
            loadingPosts: true,
			updated: '',
			childData: '',
		};		
	}

	handleCallback = (childData) => {
		this.setState({childData: childData});
		console.log("Daten vom Kind Post: ", childData);
	}
	
	//when loading the component we do axios call to backend to get all comments for the post
	componentDidMount(){
		console.log("POST: ", this.props.post.post_id);
		axios.get(this.props.url + "comments/getComments?postid=" + this.props.post.post_id, 
		)
		.then((response) => {
			this.setState({comments: response.data});
		})
		.catch((error) => {
			console.log(error);
		});
	}

	componentDidUpdate(prevProps, prevState) {
		if(prevProps.updated !== this.props.updated || prevState.childData !== this.state.childData){
			console.log("Kommentliste wurde geändert!!");
			axios.get(this.props.url + "comments/getComments?postid=" + this.props.post.post_id, 
			)
			.then((response) => {
				//clearing comment list and reload it 
				this.setState({comments: []});
				this.setState({comments: response.data});
			})
			.catch((error) => {
				console.log(error);
			});
		}
	}
  
	render() {
		const { classes } = this.props;
		return (
		<div className={classes.center}>
			{/* <h1>This component renders a list of comment components</h1> */}
			{this.state.comments.map((comment, index)=> (
				<Comment comment={comment} key={index} post={this.props.post} currentUser={this.props.currentUser} parentCallBack={this.handleCallback} url={this.props.url}></Comment>
			))}
	    </div>
		);
	}
}

export default withStyles(styles)(CommentList);

