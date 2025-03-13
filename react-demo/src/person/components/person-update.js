import React from 'react';
import validate from "./validators/person-validators";
import Button from "react-bootstrap/Button";
import * as API_USERS from "../api/person-api";
import APIResponseErrorMessage from "../../commons/errorhandling/api-response-error-message";
import { Col, Row } from "reactstrap";
import { FormGroup, Input, Label } from 'reactstrap';

class PersonUpdateForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            errorStatus: 0,
            error: null,
            formIsValid: false,
            formControls: {
                name: {
                    value: props.person ? props.person.name : '',
                    placeholder: 'What is your name?...',
                    valid: false,
                    touched: false,
                    validationRules: {
                        minLength: 3,
                        isRequired: true
                    }
                },
                address: {
                    value: props.person ? props.person.address : '',
                    placeholder: 'Email... (used for identification)',
                    valid: false,
                    touched: false,
                    validationRules: {
                        emailValidator: true
                    }
                },
                age: {
                    value: props.person ? props.person.age : '',
                    placeholder: 'Age...',
                    valid: false,
                    touched: false,
                },
            }
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    componentDidMount() {
        this.validateForm();
    }

    handleChange(event) {
        const name = event.target.name;
        const value = event.target.value;

        const updatedControls = { ...this.state.formControls };
        const updatedFormElement = updatedControls[name];

        updatedFormElement.value = value;
        updatedFormElement.touched = true;
        updatedFormElement.valid = validate(value, updatedFormElement.validationRules);
        updatedControls[name] = updatedFormElement;

        this.setState({ formControls: updatedControls }, this.validateForm);
    }

    validateForm() {
        const { formControls } = this.state;
        const formIsValid = Object.keys(formControls).every(name => formControls[name].valid);
        this.setState({ formIsValid });
    }

    updatePerson(person) {

        API_USERS.updatePersonByAddress(person, (updateResult, updateStatus, updateError) => {
            if (updateResult !== null && (updateStatus === 200 || updateStatus === 204)) {
                console.log("Successfully updated person with address: " + person);
                this.props.reloadHandler(); // Call the reload handler
            } else {
                console.error("API Error during update: ", updateError);
                this.setState({
                    errorStatus: updateStatus,
                    error: updateStatus === 404 ? "Person not found for update!" : updateError || "An unexpected error occurred during update."
                });
            }
        });
    }


    handleSubmit() {
        let person = {
            name: this.state.formControls.name.value,
            age: this.state.formControls.age.value,
            address: this.state.formControls.address.value
        };

        console.log("Updating person:", person);
        this.updatePerson(person);
    }



    render() {
        return (
            <div>
                <FormGroup id='name'>
                    <Label for='nameField'> Name: </Label>
                    <Input name='name' id='nameField' placeholder={this.state.formControls.name.placeholder}
                           onChange={this.handleChange}
                           value={this.state.formControls.name.value}
                           valid={this.state.formControls.name.valid}
                           required
                    />
                    {this.state.formControls.name.touched && !this.state.formControls.name.valid &&
                        <div className={"error-message row"}> * Name must have at least 3 characters </div>}
                </FormGroup>

                <FormGroup id='address'>
                    <Label for='addressField'> Email-Address: </Label>
                    <Input name='address' id='addressField' placeholder={this.state.formControls.address.placeholder}
                           onChange={this.handleChange}
                           value={this.state.formControls.address.value}
                           valid={this.state.formControls.address.valid}
                           required
                    />
                    {this.state.formControls.address.touched && !this.state.formControls.address.valid &&
                        <div className={"error-message"}> * Email must have a valid format</div>}
                </FormGroup>

                <FormGroup id='age'>
                    <Label for='ageField'> Age: </Label>
                    <Input name='age' id='ageField' placeholder={this.state.formControls.age.placeholder}
                           min={0} max={100} type="number"
                           onChange={this.handleChange}
                           value={this.state.formControls.age.value}
                           valid={this.state.formControls.age.valid}
                           required
                    />
                </FormGroup>

                <Row>
                    <Col sm={{ size: '4', offset: 8 }}>
                        <Button type={"submit"} disabled={!this.state.formIsValid} onClick={this.handleSubmit}> Update </Button>
                    </Col>
                </Row>

                {/* Display specific error message for person not found */}
                {this.state.errorStatus === 404 && this.state.error && (
                    <div className="error-message" style={{ color: "red", marginTop: "10px" }}>
                        {this.state.error}
                    </div>
                )}

                {/* Display general error message for other cases */}
                {this.state.errorStatus > 0 && this.state.errorStatus !== 404 && (
                    <APIResponseErrorMessage errorStatus={this.state.errorStatus} error={this.state.error} />
                )}
            </div>
        );
    }
}

export default PersonUpdateForm;
