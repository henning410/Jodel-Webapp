import React from "react";
import { withStyles } from '@material-ui/core/styles';
import CommentList from './CommentList';


const styles = theme => ({
	center: {
		display: 'block',
		marginLeft: '15px',
		marginRight: '15px',
		padding: '10px',
		// border: '2px solid black',
		background: 'gray',
		// height: "100vh",
	}
});

class Comments extends React.Component {

	constructor(props) {
		super(props);
	    this.state = {	  
            data: [],
            ownPosts: false,
            noPosition: false,
            loadingPosts: true
		};		
	}
	
  
	render() {
		const { classes } = this.props;
		return (
		<div className={classes.center}>
			{/* <h4>Here goes the comments list plus buttons for adding and voting on comments.</h4> */}
			<CommentList post={this.props.post} updated={this.props.updated} currentUser={this.props.currentUser} url={this.props.url}></CommentList>
	    </div>
		);
	}
}

export default withStyles(styles)(Comments);

