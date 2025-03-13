import React from "react";
import Table from "../../commons/tables/table";

const filters = [
    {
        accessor: 'name',
    }
];

class PersonTable extends React.Component {

    render() {
        const columns = [
            // {
            //     Header: 'ID',
            //     accessor: 'id',
            //     Cell: ({ value }) => value ? value.substring(0, 8) : "", // Afișează primele 8 caractere
            // },
            {
                Header: 'Name',
                accessor: 'name',
            },
            {
                Header: 'Address',
                accessor: 'address',
            },
            {
                Header: 'Age',
                accessor: 'age',
            },
            {
                Header: 'Actions',
                accessor: 'actions',
                Cell: ({ row }) => (
                    console.log(row),
                        <div>
                            <button onClick={() => this.props.onDelete(row.address)}>Delete</button>
                        </div>
                ),
            }
        ];

        return (
            <Table
                data={this.props.tableData}
                columns={columns}
                search={filters}
                pageSize={5}
                onRowClick={(row) => this.props.selectHandler(row.original)}
            />
        );
    }
}

export default PersonTable;
