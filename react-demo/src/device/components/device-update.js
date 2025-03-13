import React from 'react';
import validate from "./validators/device-validators";
import Button from "react-bootstrap/Button";
import * as API_USERS from "../api/device-api";
import APIResponseErrorMessage from "../../commons/errorhandling/api-response-error-message";
import { Col, Row, FormGroup, Input, Label } from 'reactstrap';

class DeviceUpdateForm extends React.Component {
    constructor(props) {
        super(props);
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
                maxHrEnCon: {
                    value: '',
                    placeholder: 'Maximum hourly energy consumption...',
                    valid: false,
                    touched: false,
                }
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

    updateDevice() {
        const { selectedID } = this.props;
        console.log("Updating device with ID:", selectedID);

        const device = {
            address: this.state.formControls.address.value,
            description: this.state.formControls.description.value,
            maxHrEnCon: this.state.formControls.maxHrEnCon.value,
        };

        console.log("Device object being sent:", device);

        API_USERS.updateDeviceByID(selectedID, device, (updateResult, updateStatus, updateError) => {
            if (updateResult !== null && (updateStatus === 200 || updateStatus === 204)) {
                console.log("Successfully updated device with ID:", selectedID);
                this.props.reloadHandler();
            } else {
                console.error("API Error during update:", updateError);
                this.setState({
                    errorStatus: updateStatus,
                    error: updateError || "An unexpected error occurred during update."
                });
            }
        });
    }
    handleSubmit() {
        this.updateDevice(); // Call updateDevice, which now only uses selectedID
    }

    render() {
        return (
            <div>
                <FormGroup id='description'>
                    <Label for='descriptionField'>Name:</Label>
                    <Input
                        name='description'
                        id='descriptionField'
                        placeholder={this.state.formControls.description.placeholder}
                        onChange={this.handleChange}
                        value={this.state.formControls.description.value}
                        touched={this.state.formControls.description.touched ? 1 : 0}
                        valid={this.state.formControls.description.valid}
                        required
                    />
                    {this.state.formControls.description.touched && !this.state.formControls.description.valid &&
                        <div className="error-message row">* Description must have at least 3 characters</div>}
                </FormGroup>

                <FormGroup id='address'>
                    <Label for='addressField'>Address:</Label>
                    <Input
                        name='address'
                        id='addressField'
                        placeholder={this.state.formControls.address.placeholder}
                        onChange={this.handleChange}
                        value={this.state.formControls.address.value}
                        touched={this.state.formControls.address.touched ? 1 : 0}
                        valid={this.state.formControls.address.valid}
                        required
                    />
                </FormGroup>

                <FormGroup id='maxHrEnCon'>
                    <Label for='maxHrEnConField'>Max Hourly Energy Consumption:</Label>
                    <Input
                        name='maxHrEnCon'
                        id='maxHrEnConField'
                        placeholder={this.state.formControls.maxHrEnCon.placeholder}
                        onChange={this.handleChange}
                        value={this.state.formControls.maxHrEnCon.value}
                        touched={this.state.formControls.maxHrEnCon.touched ? 1 : 0}
                        valid={this.state.formControls.maxHrEnCon.valid}
                        required
                    />
                    {this.state.formControls.maxHrEnCon.touched && !this.state.formControls.maxHrEnCon.valid &&
                        <div className="error-message">* maxHrEnCon must have a valid format</div>}
                </FormGroup>

                <Row>
                    <Col sm={{ size: '4', offset: 8 }}>
                        <Button type="submit" disabled={!this.state.formIsValid} onClick={this.handleSubmit}>Update</Button>
                    </Col>
                </Row>

                {this.state.errorStatus === 404 && this.state.error && (
                    <div className="error-message" style={{ color: "red", marginTop: "10px" }}>
                        {this.state.error}
                    </div>
                )}

                {this.state.errorStatus > 0 && this.state.errorStatus !== 404 && (
                    <APIResponseErrorMessage errorStatus={this.state.errorStatus} error={this.state.error} />
                )}
            </div>
        );
    }
}

export default DeviceUpdateForm;
