import React from "react";
import Table from "../../commons/tables/table";


const filters = [
    {
        accessor: 'description',
    }
];

class DeviceTable extends  React.Component{

    render() {
        const columns = [
            {
                Header: 'ID',
                accessor: 'id',
            },
            {
                Header: 'Description',
                accessor: 'description',
            },
            {
                Header: 'Address',
                accessor: 'address',
            },
            {
                Header: 'Max Hourly Energy Consumption',
                accessor: 'maxHrEnCon',
            },
            {
                Header: 'Actions',
                accessor: 'actions',
                Cell: ({row}) => (
                    console.log(row),
                        <div>
                            <button onClick={() => this.props.onDelete(row.id)}>Delete</button>
                            <button onClick={() => this.props.onEdit(row.id)}>Edit</button>
                        </div>
                ),
            }

        ];

/*

        constructor(props) {
            super(props);
            this.state = {
                tableData: this.props.tableData
            };
        }
*/


        return (
            <Table
                data={this.props.tableData}
                columns={columns}
                search={filters}
                pageSize={5}
                onRowClick={(row) => this.props.selectHandler(row.original)}
            />
        )
    }

}

export default DeviceTable;