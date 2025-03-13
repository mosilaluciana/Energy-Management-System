import React from 'react';
import validate from "./validators/device-validators";
import Button from "react-bootstrap/Button";
import * as API_USERS from "../api/device-api";
import APIResponseErrorMessage from "../../commons/errorhandling/api-response-error-message";
import {Col, Row} from "reactstrap";
import { FormGroup, Input, Label} from 'reactstrap';



class DeviceForm extends React.Component {

    constructor(props) {
        super(props);
        this.toggleForm = this.toggleForm.bind(this);
        this.reloadHandler = this.props.reloadHandler;

        this.state = {

            errorStatus: 0,
            error: null,

            formIsValid: false,

            formControls: {
                description: {
                    value: '',
                    placeholder: 'Description...',
                    valid: false,
                    touched: false,
                    validationRules: {
                        minLength: 3,
                        isRequired: true
                    }
                },
                address: {
                    value: '',
                    placeholder: 'Address...',
                    valid: false,
                    touched: false,
                    validationRules: {
                        minLength: 3,
                    }
                },
                maxHrEnCon : {
                    value: '',
                    placeholder: 'maximum hourly energy consumption...',
                    valid: false,
                    touched: false,
                },
                email: {
                    value: '',
                    placeholder: 'Email...',
                    valid: false,
                    touched: false,
                    validationRules: {
                        isEmail: true,
                        isRequired: true
                    }
                }
            }
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    toggleForm() {
        this.setState({collapseForm: !this.state.collapseForm});
    }


    handleChange = event => {

        const name = event.target.name;
        const value = event.target.value;

        const updatedControls = this.state.formControls;

        const updatedFormElement = updatedControls[name];

        updatedFormElement.value = value;
        updatedFormElement.touched = true;
        updatedFormElement.valid = validate(value, updatedFormElement.validationRules);
        updatedControls[name] = updatedFormElement;

        let formIsValid = true;
        for (let updatedFormElementName in updatedControls) {
            formIsValid = updatedControls[updatedFormElementName].valid && formIsValid;
        }

        this.setState({
            formControls: updatedControls,
            formIsValid: formIsValid
        });

    };

    registerDevice(device) {
        return API_USERS.postDevice(device, (result, status, error) => {
            if (result !== null && (status === 200 || status === 201)) {
                console.log("Successfully inserted device with id: " + result);
                this.reloadHandler();
            } else {
                this.setState(({
                    errorStatus: status,
                    error: error
                }));
            }
        });
    }

    handleSubmit() {
        let device = {
            description: this.state.formControls.description.value,
            address: this.state.formControls.address.value,
            maxHrEnCon: this.state.formControls.maxHrEnCon.value,
            email: this.state.formControls.email.value
        };

        console.log(device);
        this.registerDevice(device);
    }

    render() {
        return (
            <div>

                <FormGroup id='description'>
                    <Label for='descriptionField'> Name: </Label>
                    <Input name='description' id='descriptionField' placeholder={this.state.formControls.description.placeholder}
                           onChange={this.handleChange}
                           defaultValue={this.state.formControls.description.value}
                           touched={this.state.formControls.description.touched? 1 : 0}
                           valid={this.state.formControls.description.valid}
                           required
                    />
                    {this.state.formControls.description.touched && !this.state.formControls.description.valid &&
                    <div className={"error-message row"}> * Description must have at least 3 characters </div>}
                </FormGroup>

                <FormGroup id='address'>
                    <Label for='addressField'> Address: </Label>
                    <Input name='address' id='addressField' placeholder={this.state.formControls.address.placeholder}
                           onChange={this.handleChange}
                           defaultValue={this.state.formControls.address.value}
                           touched={this.state.formControls.address.touched? 1 : 0}
                           valid={this.state.formControls.address.valid}
                           required
                    />
                </FormGroup>

                <FormGroup id='maxHrEnCon'>
                    <Label for='maxHrEnConField'> maxHrEnCon: </Label>
                    <Input name='maxHrEnCon' id='maxHrEnConField' placeholder={this.state.formControls.maxHrEnCon.placeholder}
                           onChange={this.handleChange}
                           defaultValue={this.state.formControls.maxHrEnCon.value}
                           touched={this.state.formControls.maxHrEnCon.touched ? 1 : 0}
                           valid={this.state.formControls.maxHrEnCon.valid}
                           required
                    />
                    {this.state.formControls.maxHrEnCon.touched && !this.state.formControls.maxHrEnCon.valid &&
                        <div className={"error-message"}> * maxHrEnCon must have a valid format</div>}
                </FormGroup>

                <FormGroup id='email'>
                    <Label for='emailField'> Email: </Label>
                    <Input
                        name='email'
                        id='emailField'
                        placeholder={this.state.formControls.email.placeholder}
                        onChange={this.handleChange}
                        defaultValue={this.state.formControls.email.value}
                        touched={this.state.formControls.email.touched ? 1 : 0}
                        valid={this.state.formControls.email.valid}
                        required
                    />
                    {this.state.formControls.email.touched && !this.state.formControls.email.valid &&
                        <div className={"error-message"}> * Introduceți un email valid</div>}
                </FormGroup>


                <Row>
                        <Col sm={{size: '4', offset: 8}}>
                            <Button type={"submit"} disabled={!this.state.formIsValid} onClick={this.handleSubmit}>  Submit </Button>
                        </Col>
                    </Row>

                {
                    this.state.errorStatus > 0 &&
                    <APIResponseErrorMessage errorStatus={this.state.errorStatus} error={this.state.error}/>
                }
            </div>
        ) ;
    }
}

export default DeviceForm;
